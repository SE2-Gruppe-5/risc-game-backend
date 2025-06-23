package com.se2gruppe5.risikobackend.game.controllers;


import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.common.util.sanitychecks.TerritoryTakeoverSanityCheck;
import com.se2gruppe5.risikobackend.game.messages.ChangeTerritoryMessage;
import com.se2gruppe5.risikobackend.game.messages.CheatAccusationMessage;
import com.se2gruppe5.risikobackend.game.messages.PlayerWonMessage;
import com.se2gruppe5.risikobackend.game.messages.UpdatePhaseMessage;

import com.se2gruppe5.risikobackend.game.messages.UpdatePlayersMessage;
import com.se2gruppe5.risikobackend.game.services.GameService;

import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        Player player = gameService.getPlayer(gameUUID, playerUUID);
        player.setName(name);
        player.setColor(color);
        sseBroadcastService.broadcast(gameService.getGame(gameUUID),
                new UpdatePlayersMessage(gameService.getPlayers(gameUUID)));
    }

    @GetMapping("/{id}/phase/next")
    @ResponseStatus(HttpStatus.CREATED)
    public void changePhase(@PathVariable("id") UUID gameUUID) {
        gameService.nextPhase(gameUUID);
        sseBroadcastService.broadcast(gameService.getGame(gameUUID),
                new UpdatePhaseMessage(gameService.getPhase(gameUUID)));
        if (gameService.checkRequiresPlayerChange(gameUUID)) {
            gameService.nextPlayer(gameUUID);
            sseBroadcastService.broadcast(gameService.getGame(gameUUID),
                    new UpdatePlayersMessage(gameService.getPlayers(gameUUID)));
        }
    }

    @PostMapping("/{gameId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void getGameInfo(@PathVariable("gameId") UUID gameUUID,
                            @RequestParam("uuid") UUID playerUUID) {
        sseBroadcastService.send(playerUUID,
                new ChangeTerritoryMessage(gameService.getTerritoryList(gameUUID)));
        sseBroadcastService.send(playerUUID,
                new UpdatePlayersMessage(gameService.getPlayers(gameUUID)));
        sseBroadcastService.broadcast(gameService.getGame(gameUUID),
                new UpdatePhaseMessage(gameService.getPhase(gameUUID)));
    }


    @PatchMapping("/{id}/territory")
    @ResponseStatus(HttpStatus.CREATED)
    public void changeTerritory(@PathVariable("id") UUID gameUUID,
                                @RequestParam(required = false) UUID owner,
                                @RequestParam int id,
                                @RequestParam int stat) {
        Territory territory = gameService.getTerritory(gameUUID, id);
        TerritoryTakeoverSanityCheck.getInstance().plausible(territory, owner, stat);
        territory.setOwner(owner);
        territory.setStat(stat);
        UUID winnerID = gameService.checkWon(gameUUID);


        sseBroadcastService.broadcast(gameService.getGame(gameUUID),
                new ChangeTerritoryMessage(gameService.getTerritoryList(gameUUID)));
        if(winnerID != null) {
            sseBroadcastService.broadcast(gameService.getGame(gameUUID), new PlayerWonMessage((winnerID)));
        }
    }


    @PostMapping("/{id}/cheating")
    @ResponseStatus(HttpStatus.CREATED)
    public void getCheatingInfo(@PathVariable("id") UUID gameUUID,
                            @RequestParam("uuid") UUID playerUUID) {
        sseBroadcastService.broadcast(gameService.getGame(gameUUID),
                new CheatAccusationMessage(playerUUID));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleGameNotFound() {
        // Spring will automatically return 404 Not Found here
    }


    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleGameConflict() {
        // Spring will automatically return 409 Conflict here
    }
}
