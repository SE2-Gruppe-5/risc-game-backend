package com.se2gruppe5.risikobackend.lobby.controllers;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.services.LobbyService;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.security.SecureRandom;
import java.util.UUID;

@RestController
@RequestMapping("/lobby")
public class LobbyController {
    private final LobbyService lobbyService;
    private final SseBroadcastService sseBroadcastService;
    private final SecureRandom random = new SecureRandom();

    @Autowired
    public LobbyController(LobbyService lobbyService, SseBroadcastService sseBroadcastService) {
        this.lobbyService = lobbyService;
        this.sseBroadcastService = sseBroadcastService;
    }

    @GetMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createLobby() {
        Lobby lobby = lobbyService.createLobby();
        return lobby.code();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable String id) {
        lobbyService.deleteLobby(id);
    }

    @PutMapping("/{id}/player")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinLobby(@PathVariable String id,
                          @RequestParam UUID uuid,
                          @RequestParam String name) {
        if (!sseBroadcastService.hasSink(uuid)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Player not found");
        }

        Color c = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        lobbyService.joinLobby(id, new Player(uuid, name, c.getRGB()));
    }

    @DeleteMapping("/{id}/player")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveLobby(@PathVariable String id,
                           @RequestParam UUID uuid,
                           @RequestParam(required = false) String reason) {
        lobbyService.leaveLobby(id, uuid, reason);
    }

    @GetMapping("/{id}/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable String id) {
        lobbyService.startGame(id);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleLobbyNotFound() {
        // Spring will automatically return 404 Not Found here
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleLobbyConflict() {
        // Spring will automatically return 409 Conflict here
    }
}
