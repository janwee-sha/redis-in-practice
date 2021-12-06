package com.janwee.redisinpractice.trade.domain.test;

import com.janwee.redisinpractice.trade.domain.TradeService;
import com.janwee.redisinpractice.trade.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @Test
    void shouldTransferConsistently() {
        Account fromAcct = new Account("1", 10),
                toAcct = new Account("2", 5);
        boolean transferred = tradeService.transferFund(fromAcct, toAcct, 5);
        if (transferred) {
            assertEquals(10, toAcct.fund());
            assertEquals(5, fromAcct.fund());
        } else {
            assertEquals(5, toAcct.fund());
            assertEquals(10, fromAcct.fund());
        }
    }
}
