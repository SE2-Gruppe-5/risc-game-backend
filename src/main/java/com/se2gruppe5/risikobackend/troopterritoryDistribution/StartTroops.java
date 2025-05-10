package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class StartTroops {
    public Map<String, Map<String, Integer>> distributeStartingTroops(List<String> territories, int totalTroops) {
        if (territories.isEmpty()) {
            throw new IllegalArgumentException("Player must own at least one territory");
        }

        if (totalTroops < territories.size()) {
            throw new IllegalArgumentException("Not enough troops to allocate at least one per territory");
        }

        Random random = new Random();
        int remainingTroops = totalTroops - territories.size();

        // Initial Truppen (1 pro Territorium)
        Map<String, Integer> territoryTroops = new ConcurrentHashMap<>();
        territories.forEach(territory -> territoryTroops.put(territory, 1));

        // Verteilung der restlichen Truppen zufällig
        while (remainingTroops > 0) {
            String randomTerritory = territories.get(random.nextInt(territories.size()));
            territoryTroops.put(randomTerritory, territoryTroops.get(randomTerritory) + 1);
            remainingTroops--;
        }

        // Rückgabe der Truppenverteilung
        Map<String, Map<String, Integer>> result = new ConcurrentHashMap<>();
        result.put("troops", territoryTroops);
        return result;
    }
}
