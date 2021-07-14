package com.janwee.redisinpractice.cache.repository;

import com.janwee.redisinpractice.cache.model.Character;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CharacterRepository {
    private static final List<Character> list = Arrays.asList(new Character("Frodo", "Hobbit"),
            new Character("Aragorn", "Man"),
            new Character("Sam", "Hobbit"),
            new Character("Boromir", "Man"),
            new Character("Gandalf", "Maiar"),
            new Character("Legolas", "Elf"),
            new Character("Gimli", "Dwarf"),
            new Character("Thorin", "Dwarf"),
            new Character("Galadriel", "Elf"),
            new Character("Elrond", "Elf"));

    public Set<Character> getAllByKind(String kind) {
        return list.stream().filter(c -> kind.equals(c.getName())).collect(Collectors.toSet());
    }
}
