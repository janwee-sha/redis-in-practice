package com.janwee.redisinpractice.cache.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Set;

@Repository
public class CharacterCache {
    private Jedis jedis;

    @Autowired
    public CharacterCache(Jedis jedis) {
        this.jedis = jedis;
    }

    public Set<String> getNamesByKind(String kind) {
        return jedis.smembers(kind);
    }

    public void addNamesByKind(String kind, String[] names) {
        jedis.sadd(kind, names);
    }

    public void expire(String kind, int seconds) {
        jedis.expire(kind, seconds);
    }
}
