package com.se2gruppe5.risikobackend.TroopCount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CountryTest {

    @Test
    void testDefaultConstructorAndSetters() {
        Country country = new Country();
        country.setName("Italy");
        country.setTroops(12);

        assertEquals("Italy", country.getName());
        assertEquals(12, country.getTroops());
    }

    @Test
    void testParameterizedConstructor() {
        Country country = new Country("Brazil", 7);

        assertEquals("Brazil", country.getName());
        assertEquals(7, country.getTroops());
    }

    @Test
    void testSetName() {
        Country country = new Country();
        country.setName("Norway");
        assertEquals("Norway", country.getName());
    }

    @Test
    void testSetTroops() {
        Country country = new Country();
        country.setTroops(20);
        assertEquals(20, country.getTroops());
    }
}
