package com.se2gruppe5.risikobackend.PlayerControllers;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class PlayerService {
    private final Set<String> activePlayers = ConcurrentHashMap.newKeySet();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addActivePlayer(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be null or empty");
        }
        activePlayers.add(playerName);
        notifyClients();
    }
    public Set<String> getActivePlayers() {
        return activePlayers;
    }

    public SseEmitter registerClient() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        try {

            emitter.send(SseEmitter.event().name("players").data(activePlayers));
            System.out.println("New client registered, sending active players: " + activePlayers);
        } catch (IOException e) {

            emitters.remove(emitter);
            System.err.println("Error sending to client: " + e.getMessage());
        }


        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            System.out.println("Client disconnected.");
        });

        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            System.out.println("Client connection timed out.");
        });

        return emitter;
    }


    private void notifyClients() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("players").data(activePlayers));
            } catch (IOException e) {

                emitters.remove(emitter);
                System.err.println("Error sending to emitter: " + e.getMessage());
            }
        }
    }
}
