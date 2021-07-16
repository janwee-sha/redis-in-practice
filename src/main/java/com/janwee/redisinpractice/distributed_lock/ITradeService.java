package com.janwee.redisinpractice.distributed_lock;

public interface ITradeService {
    boolean transferFund(User fromAcct, User toAcct, int amount);
}
