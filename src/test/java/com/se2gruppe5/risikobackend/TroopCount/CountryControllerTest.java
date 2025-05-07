package com.se2gruppe5.risikobackend.TroopCount;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

public class CountryControllerTest {
    private CountryController controller;

    @BeforeEach
    void setUp() {
        controller = new CountryController();
    }

    @Test
    void testGetCountries() {
        Collection<Country> countries = controller.getCountries();
        assertEquals(3, countries.size());
    }

    @Test
    void testUpdateTroopsSuccess() {
        Country update = new Country("Germany", 20);
        ResponseEntity<Void> response = controller.updateTroops(update);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(20, controller.getCountries().stream()
                .filter(c -> c.getName().equals("Germany"))
                .findFirst().get().getTroops());
    }

    @Test
    void testUpdateTroopsNotFound() {
        Country update = new Country("Italy", 15);
        ResponseEntity<Void> response = controller.updateTroops(update);
        assertEquals(404, response.getStatusCodeValue());
    }
}
