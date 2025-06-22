package com.se2gruppe5.risikobackend.game.vibrateonterritoryloss;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;

import org.springframework.stereotype.Component;

@Component
public class GameEventDispatcher {
    private final SseBroadcastService sseBroadcastService;

    public GameEventDispatcher(SseBroadcastService sseBroadcastService) {
        this.sseBroadcastService = sseBroadcastService;
    }

    public void dispatch(Message message) {
        if (message instanceof VibrateClientEvent vibrateEvent) {
            sseBroadcastService.send(vibrateEvent.getPlayerId(), vibrateEvent);
        } else {
            // fallback: ggf. an alle oder loggen
            sseBroadcastService.broadcast(message);
        }
    }

}
