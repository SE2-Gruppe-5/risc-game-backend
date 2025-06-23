package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CheatAccusationMessageUnitTest {
    private UUID accusedPlayerUUID;

    @BeforeEach
    void setUp() {
        accusedPlayerUUID = UUID.randomUUID();
    }

    @Test
    void testConstructor() {
        CheatAccusationMessage msg = new CheatAccusationMessage(accusedPlayerUUID);
        assertEquals(accusedPlayerUUID, msg.accusedPlayerUUID());
        assertEquals(MessageType.ACCUSE_CHEATING, msg.getType());
    }

    @Test
    void testEmpty() {
        CheatAccusationMessage nullMsg = new CheatAccusationMessage(null);

        assertNull(nullMsg.accusedPlayerUUID());
        assertEquals(MessageType.ACCUSE_CHEATING, nullMsg.getType());
    }
}
