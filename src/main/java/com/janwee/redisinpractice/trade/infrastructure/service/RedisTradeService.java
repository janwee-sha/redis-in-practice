package com.janwee.redisinpractice.trade.infrastructure.service;

import com.janwee.redisinpractice.trade.domain.TradeService;
import com.janwee.redisinpractice.trade.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisTradeService implements TradeService {
    private Jedis conn;

    @Autowired
    public RedisTradeService(Jedis conn) {
        this.conn = conn;
    }

    public boolean transferFund(Account fromAcct, Account toAcct, int amount) {
        String fromAcctLock = fromAcct.accountId(), toAcctLock = toAcct.accountId();
        String fromLockId = DistributedLocks.acquireLock(conn, fromAcctLock, 1000),
                toLockId = DistributedLocks.acquireLock(conn, toAcctLock, 1000);
        if (fromLockId == null || toLockId == null) {//如果任一账户的锁获取失败则释放锁
            if (fromLockId != null) DistributedLocks.releaseLock(conn, fromAcctLock, fromLockId);
            if (toLockId != null) DistributedLocks.releaseLock(conn, toAcctLock, toLockId);
            return false;
        }

        //成功获取两个账户的锁
        try {
            return fromAcct.subtractMoney(amount) && toAcct.increaseMoney(amount);//转账
        } finally {
            //释放两个账户的锁
            DistributedLocks.releaseLock(conn, fromAcctLock, fromLockId);
            DistributedLocks.releaseLock(conn, toAcctLock, toLockId);
        }
    }
}
