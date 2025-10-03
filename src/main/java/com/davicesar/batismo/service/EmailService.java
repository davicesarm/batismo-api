package com.davicesar.batismo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Método para enviar um e-mail de verificação de conta.
     * @param destinatario O e-mail do usuário que receberá a mensagem.
     * @param token O token de verificação a ser incluído no link.
     */
    public void enviarEmailDeVerificacao(String destinatario, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String fromEmail = "no-reply@batismo.davicesar.com";
            String fromName = "Pastoral do Batismo";
            helper.setFrom(fromEmail, fromName);

            helper.setTo(destinatario);
            helper.setSubject("Confirme seu Cadastro");

            // Corpo do e-mail
            String texto = "Olá!\n\n";
            texto += "Para ativar sua conta, faça login com este email usando o seguinte token como senha:\n";
            texto += "Token: " + token;
            texto += "\n\nAtenciosamente,\nEquipe Batismo Davi Cesar.";

            helper.setText(texto);

            mailSender.send(mimeMessage);
            System.out.println("E-mail de verificação enviado com sucesso para: " + destinatario);

        } catch (Exception e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}