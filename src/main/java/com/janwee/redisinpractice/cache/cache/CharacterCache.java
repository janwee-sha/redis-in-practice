package com.janwee.redisinpractice.cache.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.List;

@Repository
public class CharacterCache {
    private Jedis jedis;

    @Autowired
    public CharacterCache(Jedis jedis) {
        this.jedis = jedis;
    }

    public List<String> getNamesByKind(String kind) {
        List<String> names = jedis.lrange(kind, 0, -1);
        if (names == null || names.size() == 0) return null;
        return names;
    }

    public void addNamesByKind(String kind, String[] names) {
        jedis.sadd(kind, names);
    }

    public void expire(String kind, int seconds) {
        jedis.expire(kind, seconds);
    }
}
