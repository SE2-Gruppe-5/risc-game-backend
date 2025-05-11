package com.se2gruppe5.risikobackend.game.repositories;

import com.se2gruppe5.risikobackend.common.repositories.AbstractRepository;
import com.se2gruppe5.risikobackend.game.objects.Game;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class GameRepositoryImpl extends AbstractRepository<UUID, Game> implements GameRepository {
}
