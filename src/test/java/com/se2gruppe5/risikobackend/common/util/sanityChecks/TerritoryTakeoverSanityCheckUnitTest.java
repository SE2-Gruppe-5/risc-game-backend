package com.se2gruppe5.risikobackend.common.util.sanityChecks;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TerritoryTakeoverSanityCheckUnitTest {
    private TerritoryTakeoverSanityCheck check;

    @BeforeEach
    public void setUp() {
        check =  new TerritoryTakeoverSanityCheck();
    }

    @Test
    public void testReinforcement() {
        UUID owner =  UUID.randomUUID();
        Territory t1 = new Territory(1, owner, 1, Continent.RAM);
        assertDoesNotThrow(() -> check.plausible(t1, owner, 1));
    }

    @Test
    public void testValidTakeover() {
        UUID player =  UUID.randomUUID();
        UUID enemy = UUID.randomUUID();

        Territory t1 = new Territory(1, player, 1, Continent.RAM);
        Territory t2 = new Territory(1, enemy, 10, Continent.CPU);
        t1.getConnections().add(t2);

        assertDoesNotThrow(() -> check.plausible(t1, enemy, 1));
    }

    @Test
    public void testInvalidTakeover() {
        UUID player =  UUID.randomUUID();
        UUID enemy = UUID.randomUUID();

        Territory t1 = new Territory(1, player, 10, Continent.RAM);
        Territory t2 = new Territory(1, enemy, 1, Continent.CPU);
        t1.getConnections().add(t2);

        assertThrows(IllegalStateException.class, () -> check.plausible(t1, enemy, 10));
    }
}
