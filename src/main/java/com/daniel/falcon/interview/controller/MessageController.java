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

@RestController
public class MessageController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MessageService messageService;

    @RequestMapping(name = "/add", method = RequestMethod.POST)
    public void addMessage(@RequestBody String message) {
        stringRedisTemplate.convertAndSend(RedisConfig.TOPIC_NAME, message);
    }

    @RequestMapping(name = "/get", method = RequestMethod.GET)
    public List<Message> getAll() {
        return messageService.getAllMessages();
    }
}
