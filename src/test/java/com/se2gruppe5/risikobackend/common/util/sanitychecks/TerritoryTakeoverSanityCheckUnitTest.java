package com.se2gruppe5.risikobackend.common.util.sanitychecks;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TerritoryTakeoverSanityCheckUnitTest {
    private final TerritoryTakeoverSanityCheck check = TerritoryTakeoverSanityCheck.getInstance();

    @Test
    void testInitialAssignment() {
        UUID player = UUID.randomUUID();
        Territory t1 = new Territory(1, null, 0, Continent.EMBEDDED_CONTROLLER);
        assertDoesNotThrow(() -> check.plausible(t1, player, 1));
    }

    @Test
    void testInitialAssignmentInvalid() {
        UUID player = UUID.randomUUID();
        Territory t1 = new Territory(1, null, 0, Continent.EMBEDDED_CONTROLLER);
        assertThrows(IllegalStateException.class, () -> check.plausible(t1, player, 0));
    }

    @Test
    void testNoOwnerStaysSame() {
        Territory t1 = new Territory(1, null, 0, Continent.SOUTHBRIDGE);
        assertDoesNotThrow(() -> check.plausible(t1, null, 0));
    }

    @Test
    void testNoOwnerInvalidTroopCount() {
        Territory t1 = new Territory(1, null, 0, Continent.SOUTHBRIDGE);
        assertThrows(IllegalStateException.class, () -> check.plausible(t1, null, 1));
    }

    @Test
    void testReinforcement() {
        UUID owner =  UUID.randomUUID();
        Territory t1 = new Territory(1, owner, 1, Continent.RAM);
        assertDoesNotThrow(() -> check.plausible(t1, owner, 1));
        assertDoesNotThrow(() -> check.plausible(t1, owner, 10));
    }

    @Test
    void testReinforcementInvalid() {
        UUID owner = UUID.randomUUID();
        Territory t1 = new Territory(1, owner, 1, Continent.RAM);
        assertThrows(IllegalStateException.class, () -> check.plausible(t1, owner, 0));
    }

    @Test
    void testInvalidClearTerritory() {
        UUID owner =  UUID.randomUUID();
        Territory t1 = new Territory(1, owner, 1, Continent.DCON);
        assertThrows(IllegalStateException.class, () -> check.plausible(t1, null, 0));
    }

    @Test
    void testValidTakeover() {
        UUID player =  UUID.randomUUID();
        UUID enemy = UUID.randomUUID();

        Territory t1 = new Territory(1, player, 1, Continent.RAM);
        Territory t2 = new Territory(2, enemy, 10, Continent.CPU);
        t1.getConnections().add(t2);

        assertDoesNotThrow(() -> check.plausible(t1, enemy, 1));
    }

    @Test
    void testInvalidTakeoverTooFewAdjacentTroops() {
        UUID player =  UUID.randomUUID();
        UUID enemy = UUID.randomUUID();

        Territory t1 = new Territory(1, player, 10, Continent.RAM);
        Territory t2 = new Territory(2, enemy, 1, Continent.CPU);
        Territory t3 = new Territory(3, player, 9, Continent.MMC);
        t1.getConnections().add(t2);
        t1.getConnections().add(t3);

        assertThrows(IllegalStateException.class, () -> check.plausible(t1, enemy, 10));
    }

    @Test
    void testInvalidTakeoverNotAdjacent() {
        UUID player =  UUID.randomUUID();
        UUID enemy =  UUID.randomUUID();

        Territory t1 = new Territory(1, player, 10, Continent.RAM);
        Territory t2 = new Territory(2, player, 10, Continent.CPU);
        Territory t3 = new Territory(3, null, 0, Continent.POWER_SUPPLY);
        Territory t4 = new Territory(4, enemy, 10, Continent.ESSENTIALS);
        t1.getConnections().add(t2);
        t1.getConnections().add(t3);
        t3.getConnections().add(t4);

        assertThrows(IllegalStateException.class, () -> check.plausible(t1, enemy, 10));
    }

    @Test
    void testInvalidTakeoverZeroStat() {
        UUID player =  UUID.randomUUID();
        UUID enemy =  UUID.randomUUID();

        Territory t1 = new Territory(1, player, 1, Continent.RAM);
        Territory t2 = new Territory(2, enemy, 10, Continent.CPU);
        t1.getConnections().add(t2);

        assertThrows(IllegalStateException.class, () -> check.plausible(t1, enemy, 0));
    }
}
