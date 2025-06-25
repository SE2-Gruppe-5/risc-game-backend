package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdatePlayersMessageUnitTest {
    private HashMap<UUID, Player> playerList;
    private UpdatePlayersMessage msg;

    @BeforeEach
    void setUp() {
        UUID p1UUID = UUID.randomUUID();
        UUID p2UUID = UUID.randomUUID();
        Player p1 = new Player(p1UUID, "TestP1", 1);
        Player p2 = new Player(p2UUID, "TestP2", 2);
        playerList = new HashMap<>();
        playerList.put(p1UUID,p1);
        playerList.put(p2UUID,p2);
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
        playerList = new HashMap<>();
        msg = new UpdatePlayersMessage(playerList);

        assertNotNull(msg.players());
        assertTrue(msg.players().isEmpty());
        assertEquals(MessageType.UPDATE_PLAYERS, msg.getType());
    }
}
