package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MessageServiceIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Test
    public void repositoryShouldBeAbleToGetData() {
        Message message = new Message();
        message.setMessageText("Test text");
        message.setId(null);
        Long id = messageRepository.save(message).getId();
        Message result = messageRepository.findOne(id);
        Assert.assertEquals(message.getMessageText(), result.getMessageText());
    }

    @Test
    public void messageServiceShouldBeAbleToPersistData() {
        Message expected = messageService.addMessage("Test text");
        Message result = messageRepository.findOne(expected.getId());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void messageServiceShouldBeAbleToReadAllMessages() {
        Message expected = messageService.addMessage("Test text");
        List<Message> result = messageService.getAllMessages();
        Assert.assertTrue(!result.isEmpty());
        Assert.assertTrue(result.contains(expected));
    }
}
