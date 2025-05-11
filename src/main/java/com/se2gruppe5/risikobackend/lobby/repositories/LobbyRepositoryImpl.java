package com.se2gruppe5.risikobackend.lobby.repositories;

import com.se2gruppe5.risikobackend.common.repositories.AbstractRepository;
import com.se2gruppe5.risikobackend.lobby.objects.Lobby;
import org.springframework.stereotype.Repository;

@Repository
public class LobbyRepositoryImpl extends AbstractRepository<String, Lobby> implements LobbyRepository {
}
