package com.janwee.redisinpractice.trade.infrastructure.service.test;

import com.janwee.redisinpractice.trade.infrastructure.service.DistributedLocks;
import com.janwee.redisinpractice.trade.infrastructure.service.JedisConfig;
import org.junit.jupiter.api.AfterEach;
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
    private static final String LOCK_KEY_PREFIX = "lock:";
    private static final String TEST_LOCK_KEY = "testLock";
    private static final int TEST_PARTITION_INDEX = 15;
    @Autowired
    private Jedis conn;

    @BeforeEach
    void setUp() {
        conn.select(TEST_PARTITION_INDEX);
        conn.del(LOCK_KEY_PREFIX + TEST_LOCK_KEY);
    }

    @AfterEach
    void cleanUp() {
        conn.select(TEST_PARTITION_INDEX);
        conn.del(LOCK_KEY_PREFIX + TEST_LOCK_KEY);
    }

    @Test
    void shouldAcquireLock() {
        System.out.println("Testing acquireLock...");
        assertNotNull(DistributedLocks.acquireLock(conn, TEST_LOCK_KEY, 1000));
        assertNull(DistributedLocks.acquireLock(conn, TEST_LOCK_KEY, 1000));
    }

    @Test
    void shouldAcquireLockWithTimeout() {
        System.out.println("Testing acquireLockWithTimeout...");
        assertNotNull(DistributedLocks.acquireLockWithTimeout(conn, TEST_LOCK_KEY, 1000,
                3000));
        assertNull(DistributedLocks.acquireLockWithTimeout(conn, TEST_LOCK_KEY, 1000,
                3000));
        try {
            TimeUnit.MILLISECONDS.sleep(3000);
            assertNotNull(DistributedLocks.acquireLockWithTimeout(conn, TEST_LOCK_KEY, 1000,
                    3000));
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    @Test
    void shouldReleaseLock() {
        System.out.println("Testing releaseLock");
        String identifier = DistributedLocks.acquireLock(conn, TEST_LOCK_KEY, 1000);
        if (identifier != null) {
            DistributedLocks.releaseLock(conn, TEST_LOCK_KEY, identifier);
            assertNotEquals(identifier, conn.get(TEST_LOCK_KEY));
        }
    }
}
