package com.janwee.redisinpractice.cache.model;

import java.util.Objects;

public class Character {
    private String name;
    private String kind;

    public Character(String name, String kind) {
        this.name = name;
        this.kind = kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return name.equals(character.name) &&
                kind.equals(character.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kind);
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }
}
