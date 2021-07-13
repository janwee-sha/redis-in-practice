package com.janwee.redisinpractice.cache.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Character {
    @Id
    private String name;
    @Column
    private String kind;
}
