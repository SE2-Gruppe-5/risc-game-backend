package com.se2gruppe5.risikobackend.lobby.controllers;

import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.services.LobbyService;
import com.se2gruppe5.risikobackend.common.objects.Player;
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
        try {
            lobbyService.deleteLobby(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/player")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinLobby(@PathVariable String id,
                          @RequestParam UUID uuid,
                          @RequestParam String name) {
        System.out.printf("%s %s %s",id,name,uuid);
        if (!sseBroadcastService.hasSink(uuid)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Player not found");
        }
        try {
            Color c = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            lobbyService.joinLobby(id, new Player(uuid, name, c.getRGB()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/player")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveLobby(@PathVariable String id,
                           @RequestParam UUID uuid,
                           @RequestParam(required = false) String reason) {
        try {
            lobbyService.leaveLobby(id, uuid, reason);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable String id) {
        try {
            lobbyService.startGame(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
