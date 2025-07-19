package br.com.raroacademy.demo.commons.email;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockAlertEmailBuilder {

    private final I18nUtil i18nUtil;

    public String buildHtmlContent(StockAlertEntity alert) {
        String equipmentName = i18nUtil.getMessage(
                "equipmenttype." + alert.getEquipmentStock().getEquipmentType().name().toLowerCase()
        );
        Integer quantity = alert.getEquipmentStock().getCurrentStock();
        Integer min = alert.getMinimumStock();
        Integer security = alert.getSecurityStock();

        return """
        <html>
          <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;">
            <div style="max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 6px rgba(0,0,0,0.1);">
              <h2 style="color: #dc3545; text-align: center;">%s</h2>
              <p style="font-size: 16px; color: #333;">%s</p>
              <table style="width: 100%%; border-collapse: collapse; margin-top: 20px;">
                <tr style="background-color: #f9f9f9;">
                  <td style="padding: 10px; font-weight: bold;">%s</td>
                  <td style="padding: 10px;">%s</td>
                </tr>
                <tr>
                  <td style="padding: 10px; font-weight: bold;">%s</td>
                  <td style="padding: 10px;">%d</td>
                </tr>
                <tr style="background-color: #f9f9f9;">
                  <td style="padding: 10px; font-weight: bold;">%s</td>
                  <td style="padding: 10px;">%d</td>
                </tr>
                <tr>
                  <td style="padding: 10px; font-weight: bold;">%s</td>
                  <td style="padding: 10px;">%d</td>
                </tr>
              </table>
              <p style="margin-top: 30px; font-size: 14px; color: #777;">%s</p>
            </div>
          </body>
        </html>
        """.formatted(
                i18nUtil.getMessage("stock.alert.email.title"),
                i18nUtil.getMessage("stock.alert.email.description"),
                i18nUtil.getMessage("stock.alert.equipment"), equipmentName,
                i18nUtil.getMessage("stock.alert.quantity"), quantity,
                i18nUtil.getMessage("stock.alert.min"), min,
                i18nUtil.getMessage("stock.alert.security"), security,
                i18nUtil.getMessage("stock.alert.email.footer")
        );
    }
}
