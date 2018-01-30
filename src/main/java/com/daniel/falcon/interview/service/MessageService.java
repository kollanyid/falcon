package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The MessageService stands as simplified connection with the MessageRepository.
 * It's able to create message from String and persist it to the DB, and get all previous messages
 * from it.
 */
@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Creates a message object from text and persist it to DB.
     * Returns the created message object.
     * @param text the message text
     * @return persisted message
     */
    Message addMessage(String text) {
        Message message = new Message();
        message.setMessageText(text);
        Message result = messageRepository.save(message);
        LOGGER.info("Message successfully saved to database. {}", text);
        return result;
    }

    /**
     * Returns previous available messages from DB.
     * @return all messages
     */
    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }
}
