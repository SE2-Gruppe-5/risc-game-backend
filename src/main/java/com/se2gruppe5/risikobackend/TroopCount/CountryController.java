package com.se2gruppe5.risikobackend.TroopCount;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private Map<String, Country> countries = new ConcurrentHashMap<>();

    public CountryController() {
        countries.put("Germany", new Country("Germany", 10));
        countries.put("France", new Country("France", 5));
        countries.put("Spain", new Country("Spain", 8));
    }

    @GetMapping
    public Collection<Country> getCountries() {
        return countries.values();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateTroops(@RequestBody Country update) {
        Country country = countries.get(update.getName());
        if (country != null) {
            country.setTroops(update.getTroops());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
