package com.janwee.redisinpractice.distributed_lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DistributedLocks {
    private static final String PREFIX = "lock:";

    public static String acquireLock(Jedis conn, String lockName, long acquireTimeout) {
        String identifier = UUID.randomUUID().toString();//每次获取锁生成唯一标识

        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {//检查获取锁的操作是否超时
            if (conn.setnx(PREFIX + lockName, identifier) == 1) return identifier;//成功获取锁

            try {
                TimeUnit.MILLISECONDS.sleep(1);//等待1毫秒后重新尝试获取锁
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;//获取锁失败
    }

    public static String acquireLockWithTimeout(Jedis conn, String lockName, long acquireTimeout, long lockTimeout) {
        String identifier = UUID.randomUUID().toString();//每次获取锁生成唯一标识
        String lockKey = PREFIX + lockName;
        int lockExpire = (int) lockTimeout / 1000;

        long end = System.currentTimeMillis() + acquireTimeout;
        while (System.currentTimeMillis() < end) {//检查获取锁的操作是否超时
            if (conn.setnx(lockKey, identifier) == 1) {
                //成功获取锁，设置获取到的锁在lockExpire毫秒后自动失效
                conn.expire(lockKey, lockExpire);
                return identifier;//
            }

            //如果锁的键没有自动过期策略，设置自动过期防止获取锁一定会失败
            if (conn.ttl(lockKey) == -1) conn.expire(lockKey, lockExpire);

            try {
                TimeUnit.MILLISECONDS.sleep(1);//等待1毫秒后重新尝试获取锁
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null;//获取锁失败
    }

    public static boolean releaseLock(Jedis conn, String lockName, String identifier) {
        String lockKey = PREFIX + lockName;

        while (true) {
            conn.watch(lockKey);
            if (identifier.equals(conn.get(lockKey))) {//检查线程是否仍然持有锁
                Transaction trans = conn.multi();
                trans.del(lockKey);//释放锁
                List<Object> result = trans.exec();
                if (result == null) continue;//有其他客户端修改了锁，重试
                return true;//进程成功释放锁
            }

            //进程已经失去了锁
            conn.unwatch();
            break;
        }
        return false;
    }
}
