package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.sse.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackTerritoryMessageUnitTest {
    AttackTerritoryMessage msg;
    Territory t1 = new Territory(1, UUID.randomUUID(), 1, Continent.CPU);
    Territory t2 = new Territory(2, UUID.randomUUID(), 1, Continent.RAM);

    @BeforeEach
    void setUp() {
        msg = new AttackTerritoryMessage(t1, t2, 5);
    }

    @Test
    void testGetType() {
        assertEquals(MessageType.ATTACK_TERRITORY, msg.getType());
    }

    @Test
    void testGetters() {
        assertEquals(t1, msg.from());
        assertEquals(t2, msg.target());
        assertEquals(5, msg.troops());
    }
}
