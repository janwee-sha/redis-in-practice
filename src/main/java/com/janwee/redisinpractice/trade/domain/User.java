package com.janwee.redisinpractice.trade.domain;

import java.util.Objects;

public class User {
    private String userId;
    private int fund;

    public User(String userId, int fund) {
        this.userId = userId;
        this.fund = fund;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public String userId() {
        return userId;
    }

    public boolean subtractMoney(int amount) {
        if (fund - amount < 0) return false;
        fund -= amount;
        return true;
    }

    public boolean increaseMoney(int amount) {
        if (fund > Integer.MAX_VALUE - amount) return false;
        fund += amount;
        return true;
    }

    public int fund() {
        return fund;
    }
}
