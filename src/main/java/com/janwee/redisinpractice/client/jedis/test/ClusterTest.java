package com.janwee.redisinpractice.client.jedis.test;

import com.janwee.redisinpractice.client.jedis.BeanConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import redis.clients.jedis.JedisCluster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ContextConfiguration(classes = {BeanConfig.class})
class ClusterTest {
    @Autowired
    private JedisCluster cluster;

    @Test
    void testString() {
        cluster.del("frodo");
        assertNull(cluster.get("frodo"));
        cluster.set("frodo", "baggins");

        assertEquals("baggins", cluster.type("frodo"));
        assertEquals(Boolean.TRUE, cluster.exists("frodo"));
    }
}
