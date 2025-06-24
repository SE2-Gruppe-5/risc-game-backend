package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdatePhaseMessageUnitTest {
    private UpdatePhaseMessage msg;

    @BeforeEach
    void setUp() {
        msg = new UpdatePhaseMessage(3);
    }

    @Test
    void testConstructorAndGetters() {
        msg = new UpdatePhaseMessage(5);
        assertEquals(5, msg.phase());
        assertEquals(MessageType.UPDATE_PHASE, msg.getType());
    }

    @Test
    void testNegativePhaseValue() {
        msg = new UpdatePhaseMessage(-1);
        assertEquals(-1, msg.phase());
        assertEquals(MessageType.UPDATE_PHASE, msg.getType());
    }
}
