package com.janwee.redisinpractice.cache.service.test;

import com.janwee.redisinpractice.cache.service.CacheNullValService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class CacheNullValServiceTest {
    @Autowired
    private CacheNullValService service;

    @Test
    void testGetNamesOfKind() {
        List<String> names = service.getNamesOfKind("Elf");
        assertNotNull(names);
        assertEquals(3, names.size());
        assertEquals("Legolas", names.get(0));
        assertEquals("Galadriel", names.get(1));
        assertEquals("Elrond", names.get(2));
    }
}
