package com.se2gruppe5.risikobackend.game.controllers;


import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.game.messages.ChangeTerritoryMessage;
import com.se2gruppe5.risikobackend.game.messages.UpdatePhaseMessage;

import com.se2gruppe5.risikobackend.game.messages.UpdatePlayersMessage;
import com.se2gruppe5.risikobackend.game.services.GameService;

import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Controller
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SseBroadcastService sseBroadcastService;

    //No create mapping, as it is instantiated by lobby

    @Autowired
    public GameController(GameService gameService, SseBroadcastService sseBroadcastService) {
        this.gameService = gameService;
        this.sseBroadcastService = sseBroadcastService;
    }

    @PutMapping("/{id}/update-player")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePlayer(@PathVariable String id,
                             @RequestParam UUID gameUUID,
                             @RequestParam Player player) {
        if (!sseBroadcastService.hasSink(gameUUID)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            gameService.updatePlayer(gameUUID, player);
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePlayersMessage(gameUUID, gameService.getPlayers(gameUUID)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/change-phase")
    @ResponseStatus(HttpStatus.CREATED)
    public void changePhase(@PathVariable String id,
                            @RequestParam UUID gameUUID) {
        if (!sseBroadcastService.hasSink(gameUUID)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            gameService.nextPhase(gameUUID);
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePhaseMessage(gameUUID, gameService.getPhase(gameUUID)));
            if (gameService.checkRequiresPlayerChange(gameUUID)) {
                gameService.nextPlayer(gameUUID);
                sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                        new UpdatePlayersMessage(gameUUID, gameService.getPlayers(gameUUID)));
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/get-info")
    @ResponseStatus(HttpStatus.CREATED)
    public void getInfo(@PathVariable String id,
                        @RequestParam UUID gameUUID,
                        @RequestParam UUID playerUUID) {
        if (!sseBroadcastService.hasSink(gameUUID)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            sseBroadcastService.broadcast(playerUUID,
                    new ChangeTerritoryMessage(gameUUID, gameService.getTerritories(gameUUID)));
            sseBroadcastService.broadcast(playerUUID,
                    new UpdatePlayersMessage(gameUUID, gameService.getPlayers(gameUUID)));
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePhaseMessage(gameUUID, gameService.getPhase(gameUUID)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PutMapping("/{id}/change-territory")
    @ResponseStatus(HttpStatus.CREATED)
    public void changeTerritory(@PathVariable String id,
                                @RequestParam UUID gameUUID,
                                @RequestParam Territory territory) {
        if (!sseBroadcastService.hasSink(gameUUID)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            gameService.changeTerritory(gameUUID, territory);
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new ChangeTerritoryMessage(gameUUID, gameService.getTerritoryList(gameUUID)));

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
