package com.se2gruppe5.risikobackend.lobby.controllers;

import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import com.se2gruppe5.risikobackend.lobby.services.LobbyService;
import com.se2gruppe5.risikobackend.common.objects.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Controller
public class LobbyController {
    private final LobbyService lobbyService;

    @Autowired
    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PutMapping("/lobby")
    public String createLobby() {
        Lobby lobby = lobbyService.createLobby();
        return lobby.code();
    }

    @DeleteMapping("/lobby/{id}")
    public void deleteLobby(@PathVariable String id) {
        try {
            lobbyService.deleteLobby(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/lobby/{id}/player")
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

    @DeleteMapping("/lobby/{id}/player")
    public void leaveLobby(@PathVariable String id,
                           @RequestParam UUID uuid) {
        try {
            lobbyService.leaveLobby(id, uuid);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
