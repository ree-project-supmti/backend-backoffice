package com.ree.sireleves.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    public void sendPasswordResetMail(
            String username,
            String clearPassword,
            boolean byAdmin
    ) {
        log.warn("""
            ========= MAIL MOCK =========
            To      : {}
            Subject : Réinitialisation mot de passe
            Mode    : {}
            Password: {}
            =============================
            """,
                username,
                byAdmin ? "ADMIN RESET" : "USER RESET",
                clearPassword
        );
    }
}
