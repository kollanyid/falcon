package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.config.RedisConfig;
import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import com.daniel.falcon.interview.util.AbstractIntegrationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
public class MessageReceiverIT extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String WEBSOCKET_URI = "ws://localhost:####/websocket";
    private static final String WEBSOCKET_TOPIC = "/topic/messages";

    private String recivedMessage;

    @Before
    public void setup() {
        recivedMessage = null;
    }


    @Test
    public void redisShouldSendDataToMessageReceiverAndPersist() throws InterruptedException {
        String testText = "Test text" + new Date().getTime();
        stringRedisTemplate.convertAndSend(RedisConfig.TOPIC_NAME, testText);

        waitUntilWithCondition(5000, 1000, () -> messageRepository.findAll().iterator().hasNext());
        List<Message> messages = messageRepository.findByMessageText(testText);
        Assert.assertTrue(!messages.isEmpty());
        Assert.assertEquals(testText, messages.get(0).getMessageText());
    }

    /**
     * This test is sending message with a RedisStringTemplate to an embedded Redis test server, and listens on the websocket.
     * The websocket should receive the given message.
     * Because of the embedded redis server the messages are arriving really slowly, therefore this test is really unstable.
     * If you would like to run it, please comment out or delete the Ignore annotation.
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Test
    @Ignore
    public void redisShouldSendDataToMessageReceiverAndNotifySocket() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        String expectedMessage = "Test text" + new Date().getTime();
        String filledURI = WEBSOCKET_URI.replace("####", "" + port);
        StompSession session = stompClient
                .connect(filledURI, new StompSessionHandlerAdapter() {
                })
                .get(3, SECONDS);

        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
        stringRedisTemplate.convertAndSend(RedisConfig.TOPIC_NAME, expectedMessage);

        waitUntilWithCondition(30000, 500, () -> recivedMessage != null);
        System.out.println("Recived message is: "+recivedMessage);
        Assert.assertTrue(recivedMessage != null);
        Assert.assertTrue(!recivedMessage.isEmpty());
        Assert.assertEquals(expectedMessage, recivedMessage);
    }

    @Override
    protected JdbcTemplate addJDBCTemplate() {
        return jdbcTemplate;
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            recivedMessage = new String((byte[]) o);
        }
    }
}
