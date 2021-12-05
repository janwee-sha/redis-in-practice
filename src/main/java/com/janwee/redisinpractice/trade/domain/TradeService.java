package com.janwee.redisinpractice.trade.domain;

public interface TradeService {
    boolean transferFund(User fromAcct, User toAcct, int amount);
}
