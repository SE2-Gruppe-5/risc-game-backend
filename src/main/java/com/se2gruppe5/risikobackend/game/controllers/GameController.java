package com.se2gruppe5.risikobackend.game.controllers;

import com.se2gruppe5.risikobackend.troopterritoryDistribution.AssignTerritories;
import com.se2gruppe5.risikobackend.troopterritoryDistribution.Game;
import com.se2gruppe5.risikobackend.troopterritoryDistribution.StartTroops;
import com.se2gruppe5.risikobackend.troopterritoryDistribution.Territory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/game")
public class GameController {

    private final Game game;

    public GameController() {
        // Beispiel-Spiel und initialisierte Spieler
        this.game = new Game(UUID.randomUUID());

        // Beispiel: Territorien und Spieler hinzufügen
        game.addTerritory(new Territory("Territory1", 0));
        game.addTerritory(new Territory("Territory2", 0));
        game.addTerritory(new Territory("Territory3", 0));
        game.addTerritory(new Territory("Territory4", 0));


        // Spieler werden extern zugewiesen
    }

    // API für die Territorien-Zuweisung
    @GetMapping("/assignTerritories")
    public Map<String, List<String>> assignTerritories() {
        List<String> players = game.getPlayers(); // Namen der Spieler holen
        List<String> territories = game.getTerritories();
        AssignTerritories assigner = new AssignTerritories();
        return assigner.assignTerritories(players, territories);
    }

    // API für die Truppenverteilung
    @GetMapping("/distributeTroops")
    public Map<String, Map<String, Integer>> distributeStartingTroops() {
        List<String> territories = game.getTerritories();
        StartTroops starter = new StartTroops();
        return starter.distributeStartingTroops(territories, 30); // Standard 30 Truppen
    }
}
