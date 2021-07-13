package com.janwee.redisinpractice.distributed_lock.test;

import com.janwee.redisinpractice.client.jedis.BeanConfig;
import com.janwee.redisinpractice.distributed_lock.DistributedLocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = {BeanConfig.class})
class DistributedLocksTest {
    private static final String PREFIX = "lock:";
    @Autowired
    private Jedis conn;

    @Test
    void testAcquireLock() {
        System.out.println("Testing acquireLock...");
        String lockName = "testLock";
        conn.del(PREFIX + lockName);
        assertNotNull(DistributedLocks.acquireLock(conn, lockName, 1000));
    }

    @Test
    void testAcquireLockWithTimeout() {
        System.out.println("Testing acquireLockWithTimeout...");
        String lockName = "testLock";
        conn.del(PREFIX + lockName);
        assertNotNull(DistributedLocks.acquireLockWithTimeout(conn, lockName, 1000, 1000));
    }

    @Test
    void testReleaseLock() {
        System.out.println("Testing releaseLock");
        String lockName = "testLock";
        conn.del(PREFIX + lockName);
        String identifier = DistributedLocks.acquireLock(conn, lockName, 1000);
        if (identifier != null) assertTrue(DistributedLocks.releaseLock(conn, lockName, identifier));
    }
}
