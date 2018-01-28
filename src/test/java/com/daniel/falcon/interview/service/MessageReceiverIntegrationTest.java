package com.daniel.falcon.interview.service;

import com.daniel.falcon.interview.config.RedisConfig;
import com.daniel.falcon.interview.model.Message;
import com.daniel.falcon.interview.repository.MessageRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MessageReceiverIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MessageRepository messageRepository;

    static final String WEBSOCKET_URI = "ws://localhost:####/websocket";
    static final String WEBSOCKET_TOPIC = "/topic/messages";

    BlockingQueue<String> blockingQueue;
    WebSocketStompClient stompClient;

    @Before
    public void setup() {
        blockingQueue = new LinkedBlockingDeque<>();
    }


    @Test
    public void redisShouldSendDataToMessageReceiverAndPersist() {
        String testText = "Test text" + new Date().getTime();
        stringRedisTemplate.convertAndSend(RedisConfig.TOPIC_NAME, testText);
        List<Message> messages = messageRepository.findByMessageText(testText);
        Assert.assertNotNull(messages);
        Assert.assertEquals(testText, messages.get(0).getMessageText());
    }

    @Test
    public void redisShouldSendDataToMessageReceiverAndNotifySocket() throws ExecutionException, InterruptedException, TimeoutException {
        stompClient = new WebSocketStompClient(new SockJsClient(
                Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()))));
        String testText = "Test text" + new Date().getTime();
        StompSession session = stompClient
                .connect(WEBSOCKET_URI.replace("####", "" + port), new StompSessionHandlerAdapter() {
                })
                .get(50, SECONDS);
        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
        stringRedisTemplate.convertAndSend(RedisConfig.TOPIC_NAME, testText);
        String result = blockingQueue.poll();
        /***
         * TODO
         * Session is unable to be notified by the receiver.
         *
         */
    }

    class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return byte[].class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            blockingQueue.offer(new String((byte[]) o));
        }
    }
}
