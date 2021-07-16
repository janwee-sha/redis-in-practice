package com.janwee.redisinpractice.distributed_lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

//使用细粒度的锁提升交易服务的性能
@Service
public class OptimizedTradeService implements ITradeService{
    private Jedis conn;

    @Autowired
    public OptimizedTradeService(Jedis conn) {
        this.conn = conn;
    }

    public boolean transferFund(User fromAcct, User toAcct, int amount) {
        String fromAcctLock = fromAcct.getUserId(), toAcctLock = toAcct.getUserId();
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
