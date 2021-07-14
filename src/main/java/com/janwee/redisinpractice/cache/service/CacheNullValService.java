package com.janwee.redisinpractice.cache.service;

import com.janwee.redisinpractice.cache.cache.CharacterCache;
import com.janwee.redisinpractice.cache.model.Character;
import com.janwee.redisinpractice.cache.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CacheNullValService {
    private CharacterCache cache;
    private CharacterRepository repo;

    @Autowired
    public CacheNullValService(CharacterCache cache, CharacterRepository repo) {
        this.cache = cache;
        this.repo = repo;
    }

    public List<String> getNamesOfKind(String kind) {
        List<String> names = cache.getNamesByKind(kind);
        if (names == null) {
            names = repo.getAllByKind(kind).stream().map(Character::getName).collect(Collectors.toList());
            String[] arr = new String[names.size()];
            cache.addNamesByKind(kind, names.toArray(arr));
            if (names.isEmpty()) {
                cache.expire(kind, 60 * 5);
            }
        }
        return names;
    }
}
