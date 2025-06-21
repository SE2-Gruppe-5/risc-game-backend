package com.se2gruppe5.risikobackend.common.util;

import com.se2gruppe5.risikobackend.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceFileLoaderUnitTest {
    private ResourceFileLoader loader;

    @BeforeEach
    void setUp() {
        loader = new ResourceFileLoader();
    }

    @Test
    void testLoadSuccessful() {
        String result = loader.load(TestConstants.TEST_RES_LOADER_PATH);
        assertEquals("Geladener Inhalt", result);
    }

    @Test
    void testLoadFailed() {
        assertThrows(IllegalArgumentException.class, () -> loader.load("non-existent.txt"));
    }
}
