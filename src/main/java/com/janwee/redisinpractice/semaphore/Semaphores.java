package com.janwee.redisinpractice.semaphore;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.ZParams;

import java.util.UUID;

public class Semaphores {
    public static String acquireFairSemaphore(Jedis conn, String semName, int limit, long timeout) {
        String identifier = UUID.randomUUID().toString();//random identifier
        String czset = semName + ":owner";
        String ctr = semName + ":counnter";

        //删除超时的信号量
        Transaction trans = conn.multi();

        trans.zremrangeByScore(semName.getBytes(), "-inf".getBytes(),
                String.valueOf((System.currentTimeMillis() - timeout)).getBytes());
        ZParams params = new ZParams();
        params.weights(1, 0);
        return null;
    }

    public static boolean releaseFairSemaphore(Jedis conn, String semName, String identifier) {
        return false;
    }
}
