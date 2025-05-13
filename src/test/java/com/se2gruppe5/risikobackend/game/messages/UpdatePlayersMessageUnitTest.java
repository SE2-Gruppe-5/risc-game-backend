package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UpdatePlayersMessageUnitTest {
    private List<Player> playerList;
    private UpdatePlayersMessage msg;

    @BeforeEach
    void setUp() {
        Player p1 = new Player(UUID.randomUUID(), "TestP1", 1);
        Player p2 = new Player(UUID.randomUUID(), "TestP2", 2);
        playerList = List.of(p1, p2);
        msg = new UpdatePlayersMessage(playerList);
    }

    @Test
    void testConstructorAndGetters() {
        msg = new UpdatePlayersMessage(playerList);
        assertEquals(2, msg.players().size());
        assertEquals(MessageType.UPDATE_PLAYERS, msg.getType());
    }

    @Test
    void testEmptyPlayerList() {
        playerList.clear();
        msg = new UpdatePlayersMessage(playerList);

        assertNotNull(msg.players());
        assertTrue(msg.players().isEmpty());
        assertEquals(MessageType.UPDATE_PLAYERS, msg.getType());
    }
}
