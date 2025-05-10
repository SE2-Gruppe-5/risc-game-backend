package com.se2gruppe5.risikobackend.game.controllers;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.game.messages.NextPhaseMessage;
import com.se2gruppe5.risikobackend.game.services.GameService;
import com.se2gruppe5.risikobackend.lobby.messages.GameStartMessage;
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

    @Autowired
    public GameController(GameService gameService, SseBroadcastService sseBroadcastService) {
        this.gameService = gameService;
        this.sseBroadcastService = sseBroadcastService;
    }

    @PutMapping("/{id}/change-phase")
    @ResponseStatus(HttpStatus.CREATED)
    public void changePhase(@PathVariable String id,
                          @RequestParam UUID uuid,
                          @RequestParam String name) {
        if (!sseBroadcastService.hasSink(uuid)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Game not found");
        }
        try {
            if(gameService.nextPhase(uuid)){
                gameService.nextPlayer(uuid);
                sseBroadcastService.broadcast(gameService.getGameById(uuid), new NextPhaseMessage(uuid));
            }
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
