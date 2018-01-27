package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void addMessage(String text) {
        Message message = new Message();
        message.setMessageText(text);
        messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }
}
