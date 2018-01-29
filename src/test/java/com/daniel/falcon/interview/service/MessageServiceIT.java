package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import com.daniel.falcon.interview.util.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class MessageServiceIT extends AbstractIntegrationTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void repositoryShouldBeAbleToGetData() throws InterruptedException {
        Message message = new Message();
        message.setMessageText("Test text");
        message.setId(null);
        Long id = messageRepository.save(message).getId();
        waitUntilWithCondition(5000, 1000, () -> messageRepository.findOne(id) != null);
        Message result = messageService.getAllMessages().get(0);
        Assert.assertEquals(message.getMessageText(), result.getMessageText());
    }

    @Test
    public void messageServiceShouldBeAbleToPersistData() throws InterruptedException {
        Message expected = messageService.addMessage("Test text");
        waitUntilWithCondition(5000, 1000,
                () -> messageRepository.findOne(expected.getId()) != null);
        Message result = messageRepository.findOne(expected.getId());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void messageServiceShouldBeAbleToReadAllMessages() throws InterruptedException {
        Message expected = messageService.addMessage("Test text");
        waitUntilWithCondition(5000, 1000, () -> messageRepository.findAll().iterator().hasNext());
        List<Message> result = messageService.getAllMessages();
        Assert.assertTrue(!result.isEmpty());
        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(result.contains(expected));
    }

    @Override
    protected JdbcTemplate addJDBCTemplate() {
        return jdbcTemplate;
    }
}
