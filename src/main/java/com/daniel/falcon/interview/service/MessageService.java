package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    public void addMessage(String text) {
        Message message = new Message();
        message.setMessageText(text);
        messageRepository.save(message);
        LOGGER.info("Message successfully saved to database. {}", text);
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }
}
