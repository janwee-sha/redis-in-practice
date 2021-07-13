package com.janwee.redisinpractice.cache.repository;

import com.janwee.redisinpractice.cache.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CharacterRepository extends JpaRepository<Character, String> {
    Set<Character> getAllByKind(String kind);
}
