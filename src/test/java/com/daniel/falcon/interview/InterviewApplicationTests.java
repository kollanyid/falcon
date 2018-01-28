package com.daniel.falcon.interview;

import com.daniel.falcon.interview.config.RedisConfig;
import com.daniel.falcon.interview.config.WebSocketConfig;
import com.daniel.falcon.interview.controller.MessageController;
import com.daniel.falcon.interview.repository.MessageRepository;
import com.daniel.falcon.interview.service.MessageReciver;
import com.daniel.falcon.interview.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class InterviewApplicationTests {

    @Autowired
    private MessageController controller;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageReciver messageReciver;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private WebSocketConfig webSocketConfig;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(messageService).isNotNull();
        assertThat(messageRepository).isNotNull();
        assertThat(messageReciver).isNotNull();
        assertThat(redisConfig).isNotNull();
        assertThat(webSocketConfig).isNotNull();
    }

}
