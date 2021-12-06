package com.janwee.redisinpractice.trade.domain;

public interface TradeService {
    boolean transferFund(Account fromAcct, Account toAcct, int amount);
}
