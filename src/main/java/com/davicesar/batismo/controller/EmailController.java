package com.davicesar.batismo.controller;

import com.davicesar.batismo.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/testeEmail")
    public ResponseEntity<Void> testeEmail(@RequestParam(value = "email") String email) {
        emailService.enviarEmailDeVerificacao(email, "123");
        return ResponseEntity.ok().build();
    }
}
