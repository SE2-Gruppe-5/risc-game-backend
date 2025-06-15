package com.se2gruppe5.risikobackend.game.services;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.common.util.IdUtil;
import com.se2gruppe5.risikobackend.game.objects.Game;
import com.se2gruppe5.risikobackend.game.repositories.GameRepository;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game createGame(Lobby lobby) {
        UUID gameId = IdUtil.generateUuid(gameRepository::hasGame);
        Game game = new Game(gameId, lobby.players(),new ArrayList<Territory>());//todo
        gameRepository.addGame(game);
        return game;
    }

    public Game getGameById(UUID gameId) {
        return gameRepository.getGame(gameId);
    }

    public boolean checkRequiresPlayerChange(UUID gameId) {
        return getGameById(gameId).getRequiresPlayerChange();
    }

    public void changeTerritory(UUID gameId, Territory territory) {
        getGameById(gameId).changeTerritory(territory);
    }

    public ArrayList<Territory> getTerritoryList(UUID gameId) {
        return getGameById(gameId).getTerritories();
    }

    public void updatePlayer(UUID gameId, Player player) {
        getGameById(gameId).updatePlayer(player);
    }

    public ConcurrentHashMap<UUID, Player> getPlayers(UUID gameId) {
        return getGameById(gameId).getPlayers();
    }

    public void nextPhase(UUID gameId) {
        getGame(gameId).nextPhase();
    }

    public void nextPlayer(UUID gameId) {
        getGame(gameId).nextPlayer();
    }


    public int getPhase(UUID gameId) {
        return getGame(gameId).getPhaseIndex();
    }

    private Game getGame(UUID gameId) {
        Game g = gameRepository.getGame(gameId);
        if (g == null) {
            throw new IllegalArgumentException("Game not found");
        }
        return g;
    }
    public void cheatConquer(UUID gameId, UUID cheatingPlayerId, int targetTerritoryId) {
        Game game = getGame(gameId);
        game.cheatConquer(cheatingPlayerId, targetTerritoryId);
        // Hier kann man z.B. Cheat-Detection-Logs oder Events triggern, wenn nötig
    }
}
