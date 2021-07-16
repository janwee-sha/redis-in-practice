package com.janwee.redisinpractice.distributed_lock.test;

import com.janwee.redisinpractice.distributed_lock.ITradeService;
import com.janwee.redisinpractice.distributed_lock.OptimizedTradeService;
import com.janwee.redisinpractice.distributed_lock.TradeService;
import com.janwee.redisinpractice.distributed_lock.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TradeServiceTest {
    @Autowired
    private TradeService service;

    @Autowired
    private OptimizedTradeService optimizedService;

    void test(ITradeService service){
        User fromAcct = new User("1", 10),
                toAcct = new User("2", 5);
        boolean succeed = service.transferFund(fromAcct, toAcct, 5);
        if (succeed) {
            assertEquals(10, toAcct.getFund());
            assertEquals(5, fromAcct.getFund());
        } else {
            assertEquals(5, toAcct.getFund());
            assertEquals(10, fromAcct.getFund());
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
