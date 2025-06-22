package com.se2gruppe5.risikobackend.game.vibrateinterritoryloss;

import com.se2gruppe5.risikobackend.game.vibrateonterritoryloss.GameEventDispatcher;
import com.se2gruppe5.risikobackend.game.vibrateonterritoryloss.VibrateClientEvent;
import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.*;


public class GameEventDispatcherTest {
    private SseBroadcastService sseBroadcastService;
    private GameEventDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        sseBroadcastService = Mockito.mock(SseBroadcastService.class);
        dispatcher = new GameEventDispatcher(sseBroadcastService);
    }

    @Test
    void dispatch_vibrateEvent_shouldCallSendWithPlayerId() {
        UUID playerId = UUID.randomUUID();
        VibrateClientEvent event = new VibrateClientEvent(playerId, 500, 10);

        dispatcher.dispatch(event);

        verify(sseBroadcastService, times(1)).send(eq(playerId), eq(event));
        verify(sseBroadcastService, never()).broadcast(any());
    }

    @Test
    void dispatch_otherMessage_shouldCallBroadcast() {
        Message otherMessage = mock(Message.class);

        dispatcher.dispatch(otherMessage);

        verify(sseBroadcastService, never()).send(any(UUID.class), any(Message.class));
        verify(sseBroadcastService, times(1)).broadcast(otherMessage);
    }
}
