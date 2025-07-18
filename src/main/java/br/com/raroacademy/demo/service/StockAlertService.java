package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.allert.MapperStockAlert;
import br.com.raroacademy.demo.domain.DTO.stock.allert.StockAlertResponseDTO;
import br.com.raroacademy.demo.domain.entities.*;
import br.com.raroacademy.demo.exception.InvalidStatusException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.StockAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockAlertService {

    private final StockAlertRepository stockAlertRepository;
    private final MapperStockAlert mapperStockAlert;
    private final I18nUtil i18nUtil;

    @Transactional(readOnly = true)
    public StockAlertResponseDTO getStockAlertById(Long id) {
        var equipmentStock = stockAlertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("stock.alert.not.found")));
        return mapperStockAlert.toResponseDTO(equipmentStock);
    }

    @Transactional(readOnly = true)
    public List<StockAlertResponseDTO> getAllStockAlerts() {
        var stockAlertList = stockAlertRepository.findAll(
                Sort.by(Sort.Direction.ASC, "equipmentStock.equipmentType")
        );
        return mapperStockAlert.toStockAlertList(stockAlertList);
    }

    @Transactional
    public void checkAndCreateAlert(EquipmentStockEntity stock) {
        EquipmentType type = stock.getEquipmentType();
        int current = stock.getQuantity();

        if (current <= type.getSecurityStock()) {
            boolean exists = stockAlertRepository.existsByEquipmentStockAndStockAlertStatusNot(
                    stock, StockAlertStatus.Resolvido
            );            if (!exists) {
                StockAlertEntity alert = StockAlertEntity.builder()
                        .equipmentStock(stock)
                        .minimumStock(type.getMinimumStock())
                        .securityStock(type.getSecurityStock())
                        .alertSentAt(Timestamp.from(Instant.now()))
                        .stockAlertStatus(StockAlertStatus.Criado)
                        .build();

                stockAlertRepository.save(alert);
            }
        }
    }

    @Transactional
    public void markAsProcessed(Long id) {
        var entity = stockAlertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("stock.alert.not.found")));

        if (entity.getStockAlertStatus() != StockAlertStatus.Criado) {
            throw new InvalidStatusException(i18nUtil.getMessage(
                    "stock.alert.invalid.status", entity.getStockAlertStatus().getLabel())
            );
        }

        entity.setStockAlertStatus(StockAlertStatus.Processado);
        stockAlertRepository.save(entity);
    }

    @Transactional
    public void markAsResolved(Long id) {
        var alert = stockAlertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("stock.alert.not.found")));

        if (alert.getStockAlertStatus() == StockAlertStatus.Resolvido) return;

        int current = alert.getEquipmentStock().getQuantity();
        if (current > alert.getMinimumStock()) {
            alert.setStockAlertStatus(StockAlertStatus.Resolvido);
            stockAlertRepository.save(alert);
            log.info("Alerta de estoque ID {} atualizado automaticamente para Resolvido.", id);
        }
    }
}
