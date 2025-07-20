package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import br.com.raroacademy.demo.repository.StockAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class StockAlertScheduler {

    private final StockAlertRepository stockAlertRepository;
    private final EmailStockAlertService emailStockAlertService;

    @Scheduled(cron = "0 0 13 * * *")
    public void sendReminderEmails() {
        List<StockAlertEntity> alerts = stockAlertRepository
                .findByStockAlertStatus(StockAlertStatus.CREATED);

        for (StockAlertEntity alert : alerts) {
            try {
                emailStockAlertService.sendStockAlertEmail(alert);
                log.info("Alerta reenviado para o estoque: {}", alert.getStock().getEquipmentType());
            } catch (Exception e) {
                log.error("Erro ao reenviar e-mail do alerta de estoque", e);
            }
        }
    }
}