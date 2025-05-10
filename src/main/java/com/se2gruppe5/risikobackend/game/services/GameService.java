package com.se2gruppe5.risikobackend.game.services;

import com.se2gruppe5.risikobackend.common.util.IdUtil;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.repositories.GameRepository;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Lobby lobby) {
        UUID gameId = IdUtil.generateUuid(gameRepository::hasGame);
        Game game = new Game(gameId, lobby.players());
        gameRepository.addGame(game);
        return game;
    }

    public Game getGameById(UUID gameId) {
        return gameRepository.getGame(gameId);
    }

    public boolean nextPhase(UUID gameId){
        return getGame(gameId).nextPhase();
    }
    public void nextPlayer(UUID gameId){
        getGame(gameId).nextPlayer();
    }

    private Game getGame(UUID gameId){
        Game g = gameRepository.getGame(gameId);
        if (g == null) {
            throw new IllegalArgumentException("Game not found");
        }
        return g;
    }



}
