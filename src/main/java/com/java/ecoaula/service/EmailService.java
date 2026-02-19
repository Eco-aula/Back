package com.java.ecoaula.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    /**
     * Sends a message body to all registered users.
     *
     * @param subject mail subject
     * @param body mail body
     */
    void sendToAllUsers(String subject, String body);

    /**
     * Sends a message body to a single recipient.
     *
     * @param to recipient email
     * @param body mail body
     */
    void send(String to, String body);
}
