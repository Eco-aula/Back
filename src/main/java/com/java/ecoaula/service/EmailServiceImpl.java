package com.java.ecoaula.service;

import com.java.ecoaula.entity.User;
import com.java.ecoaula.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String DEFAULT_FROM_EMAIL = "no-reply@ecoaula.local";

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Value("${spring.mail.username:${USER_MAIL:no-reply@ecoaula.local}}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    @Override
    public void sendToAllUsers(String subject, String body) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            send(user.getEmail(),subject, body);
        }
    }

    @Override
    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("EcoAula <" + resolveFromEmail() + ">");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    private String resolveFromEmail() {
        if (fromEmail == null) {
            return DEFAULT_FROM_EMAIL;
        }

        String candidate = fromEmail.trim();
        if (candidate.isBlank() || !candidate.contains("@")) {
            return DEFAULT_FROM_EMAIL;
        }

        return candidate;
    }
}

