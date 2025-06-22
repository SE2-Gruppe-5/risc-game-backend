package com.se2gruppe5.risikobackend.game.vibrateinterritoryloss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.se2gruppe5.risikobackend.game.vibrateonterritoryloss.GameEventDispatcher;
import com.se2gruppe5.risikobackend.game.vibrateonterritoryloss.TerritoryService;
import com.se2gruppe5.risikobackend.game.vibrateonterritoryloss.VibrateClientEvent;

public class TerritoryServiceTest {
    private GameEventDispatcher dispatcher;
    private TerritoryService territoryService;

    @BeforeEach
    void setUp() {
        dispatcher = mock(GameEventDispatcher.class);
        territoryService = new TerritoryService(dispatcher);
    }

    @Test
    void handleTerritoryLost_shouldDispatchVibrateClientEvent() {
        UUID playerId = UUID.randomUUID();
        String territoryId = "territory-42";

        territoryService.handleTerritoryLost(playerId, territoryId);

        ArgumentCaptor<VibrateClientEvent> captor = ArgumentCaptor.forClass(VibrateClientEvent.class);
        verify(dispatcher, times(1)).dispatch(captor.capture());

        VibrateClientEvent event = captor.getValue();
        assertEquals(playerId, event.getPlayerId());
        assertEquals(300, event.getDurationMs());
        assertEquals(5, event.getIntensity());
    }
}
