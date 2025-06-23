package com.se2gruppe5.risikobackend.PlayerControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @PostMapping("/activate")
    public ResponseEntity<Void> activatePlayer(@RequestParam String playerName) {
        try {
            playerService.addActivePlayer(playerName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/stream")
    public SseEmitter streamActivePlayers() {
        try {
            return playerService.registerClient();
        } catch (Exception e) {
            return new SseEmitter(0L);
        }
    }
}