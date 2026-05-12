package com.davicesar.batismo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

import java.time.LocalDate;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Método para enviar um e-mail de verificação de conta.
     * @param destinatario O e-mail do usuário que receberá a mensagem.
     * @param token O token de verificação a ser incluído no link.
     */
    @Async
    public void enviarEmailDeVerificacao(String destinatario, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String fromEmail = "no-reply@batismo.davicesar.com";
            String fromName = "Pastoral do Batismo";
            helper.setFrom(fromEmail, fromName);

            helper.setTo(destinatario);
            helper.setSubject("Confirme seu Cadastro");

            int anoAtual = LocalDate.now().getYear();

            String htmlTemplate =
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "    <style>" +
                            "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                            "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); padding: 30px; }" +
                            "        .header { text-align: center; margin-bottom: 20px; }" +
                            "        .token-box { background-color: #e6f7ff; border-left: 5px solid #007bff; padding: 15px; margin: 20px 0; text-align: center; border-radius: 4px; }" +
                            "        .token-text { font-size: 24px; font-weight: bold; color: #007bff; letter-spacing: 2px; }" +
                            "        .footer { margin-top: 30px; text-align: center; font-size: 12px; color: #777777; }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class='container'>" +
                            "        <div class='header'>" +
                            "            <h2>Ativação de Conta</h2>" +
                            "        </div>" +
                            "        <p>Olá,</p>" +
                            "        <p>Para ativar sua conta, faça login com seu e-mail e utilize o token abaixo como <strong>senha temporária</strong>:</p>" +
                            "        <div class='token-box'>" +
                            "            <small>Seu Token de Ativação:</small>" +
                            "            <p class='token-text'>%s</p>" +
                            "        </div>" +
                            "        <p><strong>Instruções:</strong> Após o primeiro login, você será solicitado(a) a definir uma nova senha permanente para garantir a segurança de sua conta.</p>" +
                            "        <p>Atenciosamente,</p>" +
                            "        <div class='footer'>" +
                            "            <p>Equipe Batismo Davi Cesar.</p>" +
                            "            <p>&copy; %d</p>" +
                            "        </div>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";

            String texto = String.format(htmlTemplate, token, anoAtual);

            helper.setText(texto);

            mailSender.send(mimeMessage);
            System.out.println("E-mail de verificação enviado com sucesso para: " + destinatario);

        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}