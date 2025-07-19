package br.com.raroacademy.demo.commons.email;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EmailBody {

    private final JavaMailSender mailSender;

    public void sendEmail(String email, Long token) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo("raro-equipamentos@gmail.com");
        helper.setSubject("Código de Redefinição de Senha");

        String htmlContent = "<html>"
                + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                + "<div style='background-color: #ffffff; padding: 20px; border-radius: 10px; max-width: 600px; margin: 0 auto;'>"
                + "<h2 style='color: #007BFF; text-align: center;'>Redefinição de Senha</h2>"
                + "<p>Olá,</p>"
                + "<p>Recebemos uma solicitação para redefinir a senha da sua conta Raro Equipamentos. Use o código abaixo para prosseguir:</p>"
                + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; text-align: center; margin: 20px 0;'>"
                + "<h3 style='color: #333333; margin: 0;'>" + token + "</h3>"
                + "</div>"
                + "<p>Se você não solicitou essa alteração, ignore este e-mail.</p>"
                + "<p>Atenciosamente,<br>Equipe de Suporte</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendConfirmationEmail(String email, Long token) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo("raro-equipamentos@gmail.com");
        helper.setSubject("Confirmação de Email");

        String confirmationLink = "http://localhost:8080/users/confirm-email?token=" + token + "&email=" + email;

        String htmlContent = "<html>"
                + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>"
                + "<div style='background-color: #ffffff; padding: 20px; border-radius: 10px; max-width: 600px; margin: 0 auto;'>"
                + "<h2 style='color: #007BFF; text-align: center;'>Confirmação de Email</h2>"
                + "<p>Olá,</p>"
                + "<p>Obrigado por se cadastrar na Raro Equipamentos. Por favor, confirme seu email clicando no link abaixo:</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href='" + confirmationLink + "' style='background-color: #007BFF; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Confirmar Email</a>"
                + "</div>"
                + "<p>Se você não se cadastrou em nosso site, ignore este e-mail.</p>"
                + "<p>Atenciosamente,<br>Equipe de Suporte</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("keferlinhares@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
