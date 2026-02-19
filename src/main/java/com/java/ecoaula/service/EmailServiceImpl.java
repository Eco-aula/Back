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

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Value("${USER_MAIL}")
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

    public void send(String to, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("EcoAula <" + fromEmail + ">");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}

