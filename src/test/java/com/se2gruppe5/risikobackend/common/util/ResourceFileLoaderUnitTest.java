package com.se2gruppe5.risikobackend.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResourceFileLoaderUnitTest {
    private ResourceFileLoader loader;

    @BeforeEach
    public void setUp() {
        loader = new ResourceFileLoader();
    }

    @Test
    public void testLoadSuccessful() {
        String result = loader.load("ResourceFileLoaderTestCase1.txt");
        assertEquals("Geladener Inhalt\n", result);
    }

    @Test
    public void testLoadFailed() {
        assertThrows(IllegalArgumentException.class, () -> loader.load("non-existent.txt"));
    }
}
