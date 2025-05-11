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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @PatchMapping("/{id}/player/{playerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updatePlayer(@PathVariable("id") UUID gameUUID,
                             @PathVariable("playerId") UUID playerUUID,
                             @RequestParam String name,
                             @RequestParam int color) {
        if (!sseBroadcastService.hasSink(gameUUID)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            Player player = new Player(playerUUID, name, color);
            gameService.updatePlayer(gameUUID, player);
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePlayersMessage(gameUUID, gameService.getPlayers(gameUUID)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PatchMapping("/{id}/phase")
    @ResponseStatus(HttpStatus.CREATED)
    public void changePhase(@PathVariable("id") UUID gameUUID) {
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

    @PostMapping("/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void getGameInfo(@PathVariable("gameId") UUID gameUUID,
                            @RequestParam("uuid") UUID playerUUID) {
        if (!sseBroadcastService.hasSink(gameUUID)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            sseBroadcastService.send(playerUUID,
                    new ChangeTerritoryMessage(gameUUID, gameService.getTerritories(gameUUID)));
            sseBroadcastService.send(playerUUID,
                    new UpdatePlayersMessage(gameUUID, gameService.getPlayers(gameUUID)));
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePhaseMessage(gameUUID, gameService.getPhase(gameUUID)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PatchMapping("/{id}/territory")
    @ResponseStatus(HttpStatus.CREATED)
    public void changeTerritory(@PathVariable("id") UUID gameUUID,
                                @RequestParam UUID owner,
                                @RequestParam int id,
                                @RequestParam int stat) {
        if (!sseBroadcastService.hasSink(gameUUID)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            Territory territory = new Territory(owner, stat, id);
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
