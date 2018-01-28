package com.daniel.falcon.interview.integration;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import com.daniel.falcon.interview.util.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

@Category(IntegrationTest.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class DataBaseIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;


    @Test
    public void repositoryShouldBeAbleToGetData() {
        Message message = new Message();
        message.setMessageText("Test text");
        message.setId(null);
        Long id = (Long) entityManager.persistAndGetId(message);
        entityManager.flush();

        Message result = messageRepository.findOne(id);
        Assert.assertEquals(message.getMessageText(), result.getMessageText());
    }

    @Test
    public void repositoryShouldBeAbleToPersistData() {
        Message message = new Message();
        message.setMessageText("Test text");
        messageRepository.save(message);
        Message result = messageRepository.findOne(message.getId());
        Assert.assertEquals("Test text", result.getMessageText());
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void repositoryShouldFailWithNullParam() {
        Message message = null;
        messageRepository.save(message);
        Assert.fail();
    }
}
