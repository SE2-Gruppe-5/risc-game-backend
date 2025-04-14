package com.se2gruppe5.risikobackend.game.repositories;

import com.se2gruppe5.risikobackend.common.repositories.Repository;
import com.se2gruppe5.risikobackend.game.objects.Game;

import java.util.UUID;

public interface GameRepository extends Repository<UUID, Game> {
    default void addGame(Game game) {
        add(game.uuid(), game);
    }

    default void removeGame(UUID id) {
        remove(id);
    }

    default boolean hasGame(UUID id) {
        return has(id);
    }

    default Game getGame(UUID id) {
        return get(id);
    }
}
