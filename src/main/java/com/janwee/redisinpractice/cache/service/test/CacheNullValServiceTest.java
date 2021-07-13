package com.janwee.redisinpractice.cache.service.test;

import com.janwee.redisinpractice.cache.service.CacheNullValService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CacheNullValServiceTest {
    @Autowired
    private CacheNullValService service;

    @Test
    void testGetNamesOfKind() {
        System.out.println(service.getNamesOfKind("Elf"));
    }
}
