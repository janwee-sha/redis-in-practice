package com.janwee.redisinpractice.trade.domain.test;

import com.janwee.redisinpractice.trade.domain.TradeService;
import com.janwee.redisinpractice.trade.infrastructure.service.OptimizedRedisTradeService;
import com.janwee.redisinpractice.trade.infrastructure.service.RedisTradeService;
import com.janwee.redisinpractice.trade.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TradeServiceTest {
    @Autowired
    private RedisTradeService service;

    @Autowired
    private OptimizedRedisTradeService optimizedService;

    void test(TradeService service){
        User fromAcct = new User("1", 10),
                toAcct = new User("2", 5);
        boolean succeed = service.transferFund(fromAcct, toAcct, 5);
        if (succeed) {
            assertEquals(10, toAcct.fund());
            assertEquals(5, fromAcct.fund());
        } else {
            assertEquals(5, toAcct.fund());
            assertEquals(10, fromAcct.fund());
        }
    }
    @Test
    void testTransferringFund() {
        System.out.println("Testing transferFund...");
        test(service);
    }

    @Test
    void testOptimizedTransferringFund(){
        System.out.println("Testing transferFund of OptimizedTradeService...");
        test(optimizedService);
    }
}
