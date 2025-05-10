package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import com.se2gruppe5.risikobackend.common.objects.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private final UUID gameId;
    private final ConcurrentHashMap<UUID, Player> players;
    private final ConcurrentHashMap<String, Territory> territories;

    public Game(UUID gameId) {
        this.gameId = gameId;
        this.players = new ConcurrentHashMap<>();
        this.territories = new ConcurrentHashMap<>();
    }

    public void addPlayer(Player player) {
        players.put(player.uuid(), player);
    }

    public void addTerritory(Territory territory) {
        territories.put(territory.getId(), territory);
    }

    public List<String> getTerritories() {
        return territories.values().stream()
                .map(Territory::getId) // getId() muss in deiner Territory-Klasse existieren
                .toList();             // Java 16+, sonst .collect(Collectors.toList())
    }

    public List<String> getPlayers() {
        return players.values().stream()
                .map(Player::name)  // name() kommt vom Java-Record
                .toList();          // Java 16+, ansonsten collect(Collectors.toList())
    }

    public void assignTerritories(Map<UUID, List<String>> assignments) {
        for (Map.Entry<UUID, List<String>> entry : assignments.entrySet()) {
            UUID playerId = entry.getKey();
            List<String> assignedTerritories = entry.getValue();

            for (String territoryId : assignedTerritories) {
                Territory territory = territories.get(territoryId);
                if (territory != null) {
                    territory.setOwner(playerId);  // Zuweisung des Besitzers
                }
            }
        }
    }

    public void distributeTroops() {
        // Truppen verteilen – beispielweise 30 Truppen pro Spieler auf die Territorien verteilen
        for (Territory territory : territories.values()) {
            if (territory.getOwner() != null) {
                // Hier wäre eine Logik zum Verteilen der Truppen, z.B. 1 Truppe pro Territorium
                territory.setStat(1); // Beispiel: 1 Truppe pro Territorium
            }
        }
    }
}
