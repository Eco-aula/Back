package com.java.ecoaula.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    public void sendToAllUsers(String subject, String body);
    public void send(String to, String subject, String body);
}
