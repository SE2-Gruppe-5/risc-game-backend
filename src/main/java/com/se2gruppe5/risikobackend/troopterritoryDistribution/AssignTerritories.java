package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
public class AssignTerritories {
    public Map<String, List<String>> assignTerritories(List<String> players, List<String> allTerritories) {
        if (players.isEmpty()) {
            throw new IllegalArgumentException("There must be at least one player");
        }

        if (allTerritories.size() % players.size() != 0) {
            throw new IllegalArgumentException("Territories must divide evenly among players");
        }

        Random random = new Random();
        // Mischen der Territorien
        List<String> shuffledTerritories = List.copyOf(allTerritories);
        shuffledTerritories.sort((a, b) -> random.nextInt(3) - 1);  // Zufällig sortieren

        // Berechnung der Anzahl an Territorien pro Spieler
        int territoriesPerPlayer = allTerritories.size() / players.size();

        // Zuweisung der Territorien an die Spieler (ConcurrentHashMap)
        Map<String, List<String>> territoriesAssignment = new ConcurrentHashMap<>();
        players.forEach(player -> {
            int start = players.indexOf(player) * territoriesPerPlayer;
            int end = start + territoriesPerPlayer;
            territoriesAssignment.put(player, shuffledTerritories.subList(start, end));
        });

        return territoriesAssignment;
    }
}
