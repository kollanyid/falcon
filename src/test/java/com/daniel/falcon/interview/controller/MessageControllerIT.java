package com.daniel.falcon.interview.controller;

import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import com.daniel.falcon.interview.util.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class MessageControllerIT extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void addShouldPersistMessageToDatabase() throws InterruptedException {
        String message = "Message text" + new Date().getTime();
        TestRestTemplate restTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(message, headers);
        restTemplate.exchange(
                createURLWithPort("/add"),
                HttpMethod.POST, entity, String.class);
        waitUntilWithCondition(5000, 1000, () ->
                messageRepository.findAll().iterator().hasNext());
        List<Message> expectedMessages = messageRepository.findByMessageText(message);
        Assert.assertNotNull(expectedMessages);
    }

    @Test
    public void getShouldRetrieveAllMessages() throws InterruptedException {
        Message message = new Message();
        message.setMessageText("Test message");
        message = messageRepository.save(message);
        TestRestTemplate restTemplate = new TestRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<List<Message>> responseEntity = restTemplate.exchange(
                createURLWithPort("/get"),
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<Message>>() {
                });
        waitUntilWithCondition(5000, 1000, () -> responseEntity.getStatusCode().equals(HttpStatus.OK));
        List<Message> expected = responseEntity.getBody();
        Assert.assertTrue(expected.size() == 1);
        Assert.assertTrue(expected.contains(message));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Override
    protected JdbcTemplate addJDBCTemplate() {
        return jdbcTemplate;
    }
}
