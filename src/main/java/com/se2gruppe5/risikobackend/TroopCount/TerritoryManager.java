package com.se2gruppe5.risikobackend.TroopCount;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TerritoryManager {

    private final Map<String, TerritoryRecord> territories = new ConcurrentHashMap<>();

    public TerritoryManager() {
        // Beispielinitialisierung
        territories.put("Germany", new TerritoryRecord("Germany", "player1", 10));
        territories.put("France", new TerritoryRecord("France", "player2", 5));
        territories.put("Spain", new TerritoryRecord("Spain", "player1", 8));
    }

    public List<TerritoryRecord> getAllTerritories() {
        return new ArrayList<>(territories.values());
    }

    public List<TerritoryRecord> getTerritoriesOwnedByPlayer(String playerId) {
        return territories.values().stream()
                .filter(t -> t.getOwnerId().equals(playerId))
                .collect(Collectors.toList());
    }

    public TerritoryRecord getTerritory(String name) {
        return territories.get(name);
    }

    public void updateTroops(String name, int newTroopCount) {
        TerritoryRecord record = territories.get(name);
        if (record != null) {
            record.setTroops(newTroopCount);
        }
    }
}
