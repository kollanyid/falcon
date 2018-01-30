package com.daniel.falcon.interview.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Message receiver will be notified by MessageListenerAdapter during execution.
 * It will send the message text to MessageService,
 * and push the message to the websocket with SimpMessagingTemplate
 *
 * @see MessageService
 * @see SimpMessagingTemplate
 * @see MessageListenerAdapter
 */
@Service
public class MessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private SimpMessagingTemplate template;

    public void receiveMessage(String message) {
        LOGGER.info("Message recived: {}", message);
        messageService.addMessage(message);
        template.convertAndSend("/topic/messages",message);
    }
}
