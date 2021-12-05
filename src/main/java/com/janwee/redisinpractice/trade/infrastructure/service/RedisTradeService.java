package com.janwee.redisinpractice.trade.infrastructure.service;

import com.janwee.redisinpractice.trade.domain.TradeService;
import com.janwee.redisinpractice.trade.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisTradeService implements TradeService {
    private Jedis conn;

    @Autowired
    public RedisTradeService(Jedis conn) {
        this.conn = conn;
    }

    public boolean transferFund(User fromAcct, User toAcct, int amount) {
        final String lockName = "tradeService";
        String identifier = DistributedLocks.acquireLock(conn, lockName, 1000);
        if (identifier == null) return false;
        try {
            return fromAcct.subtractMoney(amount) && toAcct.increaseMoney(amount);
        } finally {
            DistributedLocks.releaseLock(conn, lockName, identifier);
        }
    }
}
