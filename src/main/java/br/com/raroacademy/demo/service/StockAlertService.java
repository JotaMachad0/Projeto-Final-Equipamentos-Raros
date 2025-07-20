package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.allert.MapperStockAlert;
import br.com.raroacademy.demo.domain.DTO.stock.allert.StockAlertResponseDTO;
import br.com.raroacademy.demo.domain.entities.*;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import br.com.raroacademy.demo.exception.InvalidStatusException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.StockAlertRepository;
import br.com.raroacademy.demo.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final StockRepository stockRepository;
    private final MapperStockAlert mapperStockAlert;
    private final EmailStockAlertService emailStockAlertService;
    private final I18nUtil i18nUtil;

    @Transactional(readOnly = true)
    public StockAlertResponseDTO getStockAlertById(Long id) {
        var stock = stockAlertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("stock.alert.not.found")));
        return mapperStockAlert.toResponseDTO(stock);
    }

    @Transactional(readOnly = true)
    public List<StockAlertResponseDTO> getAllStockAlerts() {
        var stockAlertList = stockAlertRepository.findAllSortedByEquipmentTypeLabel();
        return mapperStockAlert.toStockAlertList(stockAlertList);
    }

    @Transactional
    public void checkAndCreateAlert(StockEntity stock) {
        int current = stock.getCurrentStock();
        int security = stock.getSecurityStock();

        if (current <= security) {
            boolean exists = stockAlertRepository.existsByStockAndStockAlertStatusNot(
                    stock, StockAlertStatus.RESOLVED
            );
            if (!exists) {
                var alert = StockAlertEntity.builder()
                        .stock(stock)
                        .alertSentAt(Timestamp.from(Instant.now()))
                        .stockAlertStatus(StockAlertStatus.CREATED)
                        .build();

                stockAlertRepository.save(alert);
                try {
                    emailStockAlertService.sendStockAlertEmail(alert);
                } catch (Exception e) {
                    log.error(i18nUtil.getMessage("stock.alert.email.error"), e);
                }
            }
        }
    }

    @Transactional
    public void markAsProcessed(Long id) {
        var entity = stockAlertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("stock.alert.not.found")));

        if (entity.getStockAlertStatus() != StockAlertStatus.CREATED) {
            String statusKey = "stock.alert.status." + entity.getStockAlertStatus().name().toLowerCase();
            String translatedStatus = i18nUtil.getMessage(statusKey);

            throw new InvalidStatusException(
                    i18nUtil.getMessage("stock.alert.invalid.status", translatedStatus)
            );
        }

        entity.setStockAlertStatus(StockAlertStatus.PROCESSED);
        stockAlertRepository.save(entity);
    }

    public void markAsResolved(Long id) {
        var alert = stockAlertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("stock.alert.not.found")));

        if (alert.getStockAlertStatus() == StockAlertStatus.RESOLVED) return;

        var stock = stockRepository.findByEquipmentType(alert.getStock().getEquipmentType());
        if (stock == null) return;

        if (alert.getStock().getCurrentStock() > stock.getMinStock()) {
            alert.setStockAlertStatus(StockAlertStatus.RESOLVED);
            stockAlertRepository.save(alert);
            log.info(i18nUtil.getMessage("stock.alert.resolved.auto", id));
        }
    }
}
