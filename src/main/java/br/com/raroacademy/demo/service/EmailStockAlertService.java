package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.email.EmailBody;
import br.com.raroacademy.demo.commons.email.StockAlertEmailBuilder;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmailStockAlertService {

    private final EmailBody emailBody;
    private final UserRepository userRepository;
    private final I18nUtil i18nUtil;
    private final StockAlertEmailBuilder emailBuilder;

    public void sendStockAlertEmail(StockAlertEntity alert) throws Exception {
        List<UserEntity> confirmedUsers = userRepository.findAllByEmailConfirmedTrue();
        String equipmentName = i18nUtil.getMessage(
                "equipmenttype." + alert.getStock().getEquipmentType().name().toLowerCase()
        );
        String subject = MessageFormat.format(i18nUtil.getMessage("stock.alert.email.subject"), equipmentName);
        String htmlContent = emailBuilder.buildHtmlContent(alert);

        for (UserEntity user : confirmedUsers) {
            emailBody.sendHtmlEmail(user.getEmail(), subject, htmlContent);
        }
    }
}