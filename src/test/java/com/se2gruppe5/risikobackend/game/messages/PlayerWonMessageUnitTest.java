package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerWonMessageUnitTest {

    private UUID id;
    PlayerWonMessage msg;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        msg = new PlayerWonMessage(id);
    }

    @Test
    void testConstructorAndGetters() {

        assertEquals(id, msg.winner());
        assertEquals(MessageType.PLAYER_WON, msg.getType());
    }


}
