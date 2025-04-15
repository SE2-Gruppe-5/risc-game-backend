package com.se2gruppe5.risikobackend.lobby.controllers;

import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.services.LobbyService;
import com.se2gruppe5.risikobackend.common.objects.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Controller
@RequestMapping("/lobby")
public class LobbyController {
    private final LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
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
        try {
            lobbyService.joinLobby(id, new Player(uuid, name));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/player")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveLobby(@PathVariable String id,
                           @RequestParam UUID uuid) {
        try {
            lobbyService.leaveLobby(id, uuid);
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
