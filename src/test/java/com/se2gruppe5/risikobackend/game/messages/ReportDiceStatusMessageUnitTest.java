package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportDiceStatusMessageUnitTest {
    ReportDiceStatusMessage msg;

    @BeforeEach
    void setUp() {
        msg = new ReportDiceStatusMessage(List.of(1, 5, 6));
    }

    @Test
    void testGetType() {
        assertEquals(MessageType.REPORT_DICE_STATUS, msg.getType());
    }

    @Test
    void testGetters() {
        assertEquals(List.of(1, 5, 6), msg.results());
    }
}
