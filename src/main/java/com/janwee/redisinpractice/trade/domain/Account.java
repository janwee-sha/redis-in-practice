package com.janwee.redisinpractice.trade.domain;

import java.util.Objects;

public class Account {
    private String accountId;
    private int fund;

    public Account(String accountId, int fund) {
        if (fund < 0) {
            throw new IllegalArgumentException("fund should not be negative.");
        }
        this.accountId = accountId;
        this.fund = fund;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account user = (Account) o;
        return accountId.equals(user.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    public String accountId() {
        return accountId;
    }

    public boolean subtractMoney(int amount) {
        if (amount < 0 || amount > fund) return false;
        fund -= amount;
        return true;
    }

    public boolean increaseMoney(int amount) {
        if (amount < 0 || amount > Integer.MAX_VALUE - fund) return false;
        fund += amount;
        return true;
    }

    public int fund() {
        return fund;
    }
}
