package com.se2gruppe5.risikobackend.game.vibrateonterritoryloss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TerritoryService {
    private final GameEventDispatcher dispatcher;
    @Autowired
    public TerritoryService(GameEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void handleTerritoryLost(UUID playerId, String territoryId) {
        // Business Logic...
        VibrateClientEvent event = new VibrateClientEvent(playerId, 300, 5);
        dispatcher.dispatch(event);
    }
}
