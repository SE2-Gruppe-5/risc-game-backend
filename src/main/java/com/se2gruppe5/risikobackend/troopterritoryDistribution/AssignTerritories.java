package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import java.util.ArrayList;
import java.util.Collections;
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

        List<String> shuffledTerritories = new ArrayList<>(allTerritories); // <-- fix
        Collections.shuffle(shuffledTerritories, new Random());

        int territoriesPerPlayer = allTerritories.size() / players.size();
        Map<String, List<String>> territoriesAssignment = new ConcurrentHashMap<>();

        for (int i = 0; i < players.size(); i++) {
            String player = players.get(i);
            int start = i * territoriesPerPlayer;
            int end = start + territoriesPerPlayer;
            territoriesAssignment.put(player, shuffledTerritories.subList(start, end));
        }

        return territoriesAssignment;
    }
}
