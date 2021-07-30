package com.janwee.redisinpractice.client.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class BeanConfig {
    private static final String HOST1 = "192.168.234.128",
            HOST2 = "172.19.0.1";

    //Client for a single redis node
    @Bean
    public Jedis jedis() {
        return new Jedis(HOST1, 6379);
    }

    //Client for a redis cluster
    @Bean
    public JedisCluster cluster() {
        Set<HostAndPort> set = new HashSet<>();
        //add all or a part of redis nodes to set
        set.add(new HostAndPort(HOST2, 16379));
        set.add(new HostAndPort(HOST2, 16380));
        set.add(new HostAndPort(HOST2, 16381));
        set.add(new HostAndPort(HOST2, 16382));
        set.add(new HostAndPort(HOST2, 16383));
        set.add(new HostAndPort(HOST2, 16384));
        return new JedisCluster(set, 1000, 1000, 5,
                new GenericObjectPoolConfig());
    }
}
