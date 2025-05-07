package com.se2gruppe5.risikobackend.TroopCount;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TerritoryManager {

    private final Map<Integer, TerritoryRecord> territories = new ConcurrentHashMap<>();

    public TerritoryManager() {
        // Beispielinitialisierung
        territories.put(1, new TerritoryRecord(1, "player1", 10));
        territories.put(2, new TerritoryRecord(2, "player2", 5));
        territories.put(3, new TerritoryRecord(3, "player1", 8));
    }

    public List<TerritoryRecord> getAllTerritories() {
        return new ArrayList<>(territories.values());
    }

    public List<TerritoryRecord> getTerritoriesOwnedByPlayer(String playerId) {
        return territories.values().stream()
                .filter(t -> t.getOwner().equals(playerId))
                .collect(Collectors.toList());
    }

    public TerritoryRecord getTerritory(int id) {
        return territories.get(id);
    }

    public void updateTroops(int id, int newTroopCount) {
        TerritoryRecord record = territories.get(id);
        if (record != null) {
            record.setTroops(newTroopCount);
        }
    }
}
