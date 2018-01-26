package com.daniel.falcon.interview.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageReciver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReciver.class);

    @Autowired
    private MessageService messageService;

    public void receiveMessage(String message) {
        LOGGER.info("Message recived: {}", message);
        messageService.addMessage(message);
    }
}