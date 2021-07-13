package com.janwee.redisinpractice.client.jedis.test;

import com.janwee.redisinpractice.client.jedis.BeanConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {BeanConfig.class})
class StandaloneTest {
    @Autowired
    private Jedis jedis;

    @Test
    void testString() {
        jedis.del("i");
        jedis.incrBy("i", 30);

        assertEquals("string", jedis.type("i"));
        assertEquals("30", jedis.get("i"));
        assertEquals(Boolean.TRUE, jedis.exists("i"));
    }

    @Test
    void testList() {
        jedis.del("list");
        jedis.lpush("list", "a", "b", "c", "d", "E");

        assertEquals("list", jedis.type("list"));
        List<String> list = jedis.lrange("list", 0, -1);
        assertEquals("E", list.get(0));
        assertEquals("c", list.get(2));
        assertEquals("a", list.get(4));
        assertEquals("a", jedis.lindex("list", 4));
        assertEquals("E", jedis.lpop("list"));
    }

    @Test
    void testSet() {
        jedis.del("set");
        assertEquals(2, jedis.sadd("set", "a", "b"));
        assertEquals(1, jedis.sadd("set", "c", "c"));

        assertTrue(jedis.sismember("set", "a"));
        assertFalse(jedis.sismember("set", "d"));

        Set<String> set = jedis.smembers("set");
        assertEquals(3, set.size());
    }

    @Test
    void testZSet() {
        jedis.del("zset");
        jedis.zadd("zset", -20, "a");
        jedis.zadd("zset", 20, "b");
        jedis.zadd("zset", 10, "c");

        assertEquals(new Tuple("b", 20.0), jedis.zpopmax("zset"));
        assertEquals(new Tuple("a", -20.0), jedis.zpopmin("zset"));
    }

    @Test
    void testHash() {
        jedis.del("frodo");
        Map<String, String> frodo = new HashMap<>();
        frodo.put("name", "Frodo Baggins");
        frodo.put("address", "Bag end,Shire");
        jedis.hset("frodo", frodo);

        assertEquals(frodo, jedis.hgetAll("frodo"));
        assertEquals("Frodo Baggins", jedis.hget("frodo", "name"));
    }

}
