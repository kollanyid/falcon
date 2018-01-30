package com.daniel.falcon.interview.controller;

import com.daniel.falcon.interview.config.RedisConfig;
import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controller for the application.
 * Has a post (/add) and a get (/get) endpoint to handle incoming new messages and return previous ones.
 */
@RestController
public class MessageController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessageService messageService;

    /**
     * Endpoint to add one message.
     * Sends the message text with StringRedisTemplate.
     *
     * @param message message text
     * @see StringRedisTemplate
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addMessage(@RequestBody String message) {
        stringRedisTemplate.convertAndSend(RedisConfig.TOPIC_NAME, message);
    }

    /**
     * Returns all previously added messages.
     * Calls in to MessageService to perform the operation.
     *
     * @return messages
     * @see MessageService
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<Message> getAll() {
        return messageService.getAllMessages();
    }
}
