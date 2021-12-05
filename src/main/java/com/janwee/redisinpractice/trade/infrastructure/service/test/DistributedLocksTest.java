package com.janwee.redisinpractice.trade.infrastructure.service.test;

import com.janwee.redisinpractice.trade.infrastructure.service.DistributedLocks;
import com.janwee.redisinpractice.trade.infrastructure.service.JedisConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = {JedisConfig.class})
class DistributedLocksTest {
    private static final String PREFIX = "lock:";
    @Autowired
    private Jedis conn;

    @BeforeEach
    void initialize() {
        conn.select(15);
    }

    @Test
    void testAcquireLock() {
        System.out.println("Testing acquireLock...");
        String lockName = "testLock";
        conn.del(PREFIX + lockName);
        assertNotNull(DistributedLocks.acquireLock(conn, lockName, 1000));
        assertNull(DistributedLocks.acquireLock(conn, lockName, 1000));
    }

    @Test
    void testAcquireLockWithTimeout() {
        System.out.println("Testing acquireLockWithTimeout...");
        String lockName = "testLock";
        conn.del(PREFIX + lockName);
        assertNotNull(DistributedLocks.acquireLockWithTimeout(conn, lockName, 1000, 3000));
        assertNull(DistributedLocks.acquireLockWithTimeout(conn, lockName, 1000, 3000));
        try {
            TimeUnit.MILLISECONDS.sleep(3000);
            assertNotNull(DistributedLocks.acquireLockWithTimeout(conn, lockName, 1000, 3000));
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    @Test
    void testReleaseLock() {
        System.out.println("Testing releaseLock");
        String lockName = "testLock";
        String lockKey = PREFIX + lockName;
        conn.del(lockKey);
        String identifier = DistributedLocks.acquireLock(conn, lockName, 1000);
        if (identifier != null) {
            DistributedLocks.releaseLock(conn, lockName, identifier);
            assertNotEquals(identifier, conn.get(lockKey));
        }
    }
}