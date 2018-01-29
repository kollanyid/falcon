package com.daniel.falcon.interview.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.SocketUtils;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;
import java.io.IOException;

@Configuration
public class TestConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfiguration.class);
    private static RedisServer redisServer;

    @Bean
    @Primary
    public JedisConnectionFactory connectionFactory() throws IOException {
        int redisPort = SocketUtils.findAvailableTcpPort();
        redisServer = new RedisServer(redisPort);
        redisServer.start();
        LOGGER.info("Embedded Redis server started");
        JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
        jedisConFactory.setHostName("localhost");
        jedisConFactory.setPort(redisPort);
        return jedisConFactory;
    }

    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
        LOGGER.info("Embedded Redis server stopped");
    }


}
