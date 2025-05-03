package com.se2gruppe5.risikobackend.TroopCount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/territories")
public class TerritoryController {

    private final TerritoryManager territoryManager;

    @Autowired
    public TerritoryController(TerritoryManager territoryManager) {
        this.territoryManager = territoryManager;
    }

    @GetMapping("/player/{playerId}")
    public List<TerritoryRecord> getTerritoriesForPlayer(@PathVariable String playerId) {
        return territoryManager.getTerritoriesOwnedByPlayer(playerId);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateTroops(@RequestBody TerritoryRecord update) {
        if (territoryManager.getTerritory(update.getName()) != null) {
            territoryManager.updateTroops(update.getName(), update.getTroops());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
