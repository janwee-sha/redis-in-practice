package com.janwee.redisinpractice.trade.infrastructure.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {
    private static final String HOST1 = "192.168.136.133";

    //Client for a single redis node
    @Bean
    public Jedis jedis() {
        return new Jedis(HOST1, 6379);
    }
}
