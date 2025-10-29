package com.david.moneymanager.services;

public interface EmailService {
    
    void sendEmail(String to, String subject, String text);
}
