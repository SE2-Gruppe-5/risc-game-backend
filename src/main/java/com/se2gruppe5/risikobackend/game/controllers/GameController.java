package com.se2gruppe5.risikobackend.game.controllers;


import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.common.util.sanityChecks.TerritoryTakeoverSanityCheck;
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

import org.springframework.web.bind.annotation.GetMapping;
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
        try {
            Player player = gameService.getPlayerById(gameUUID, playerUUID);
            player.setName(name);
            player.setColor(color);
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePlayersMessage(gameService.getPlayers(gameUUID)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{id}/phase/next")
    @ResponseStatus(HttpStatus.CREATED)
    public void changePhase(@PathVariable("id") UUID gameUUID) {
        try {
            gameService.nextPhase(gameUUID);
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePhaseMessage(gameService.getPhase(gameUUID)));
            if (gameService.checkRequiresPlayerChange(gameUUID)) {
                gameService.nextPlayer(gameUUID);
                sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                        new UpdatePlayersMessage(gameService.getPlayers(gameUUID)));
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
        try {
            sseBroadcastService.send(playerUUID,
                    new ChangeTerritoryMessage(gameService.getTerritoryList(gameUUID)));
            sseBroadcastService.send(playerUUID,
                    new UpdatePlayersMessage(gameService.getPlayers(gameUUID)));
            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new UpdatePhaseMessage(gameService.getPhase(gameUUID)));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PatchMapping("/{id}/territory")
    @ResponseStatus(HttpStatus.CREATED)
    public void changeTerritory(@PathVariable("id") UUID gameUUID,
                                @RequestParam(required = false) UUID owner,
                                @RequestParam int id,
                                @RequestParam int stat) {

        try {
            Territory territory = gameService.getTerritoryById(gameUUID, id);
            new TerritoryTakeoverSanityCheck().plausible(territory, gameService.getTerritoryList(gameUUID), owner, stat);
            territory.setOwner(owner);
            territory.setStat(stat);

            sseBroadcastService.broadcast(gameService.getGameById(gameUUID),
                    new ChangeTerritoryMessage(gameService.getTerritoryList(gameUUID)));

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
