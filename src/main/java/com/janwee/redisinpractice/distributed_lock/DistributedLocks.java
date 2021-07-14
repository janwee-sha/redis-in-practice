package com.janwee.redisinpractice.distributed_lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DistributedLocks {
    private static final String PREFIX = "lock:";

    public static String acquireLock(Jedis conn, String lockName, long acquireTimeout) {
        String identifier = UUID.randomUUID().toString();

        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {
            if (conn.setnx(PREFIX + lockName, identifier) == 1) return identifier;

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    public static String acquireLockWithTimeout(Jedis conn, String lockName, long acquireTimeout, long lockTimeout) {
        String identifier = UUID.randomUUID().toString();
        String lockKey = PREFIX + lockName;
        int lockExpire = (int) lockTimeout / 1000;

        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {
            if (conn.setnx(lockKey, identifier) == 1) {
                conn.expire(lockKey, lockExpire);
                return identifier;
            }
            if (conn.ttl(lockKey) == -1) conn.expire(lockKey, lockExpire);

            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }

    public static boolean releaseLock(Jedis conn, String lockName, String identifier) {
        String lockKey = PREFIX + lockName;

        while (true) {
            conn.watch(lockKey);
            if (identifier.equals(conn.get(lockKey))) {
                Transaction trans = conn.multi();
                trans.del(lockKey);
                List<Object> result = trans.exec();
                if (result == null) continue;
                return true;
            }

            conn.unwatch();
            break;
        }
        return false;
    }
}
