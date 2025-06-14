package com.se2gruppe5.risikobackend.common.util;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.common.objects.helpers.Position;
import com.se2gruppe5.risikobackend.common.objects.helpers.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardLoaderUnitTest {
    private final ResourceFileLoader loader = new ResourceFileLoader();
    private final String testBoard = loader.load("testBoard.json");
    private final String testBoardDuplicateConn = loader.load("testBoardDuplicateConnections.json");

    private BoardLoader boardLoader;

    @BeforeEach
    public void setUp() {
        boardLoader = new BoardLoader();
    }

    @Test
    public void allTerritoriesLoadedTest() {
        List<Territory> territories = boardLoader.loadTerritories(testBoard);
        HashMap<Integer, Territory> territoriesMap = territoriesToMap(territories);

        assertTrue(territoriesMap.containsKey(1));
        assertTrue(territoriesMap.containsKey(2));
        assertTrue(territoriesMap.containsKey(3));
    }

    @Test
    public void territoryDetailsCorrectTest() {
        List<Territory> territories = boardLoader.loadTerritories(testBoard);
        HashMap<Integer, Territory> territoriesMap = territoriesToMap(territories);

        Territory t1 =  territoriesMap.get(1);
        Territory t2 =  territoriesMap.get(2);
        Territory t3 =  territoriesMap.get(3);

        assertEquals(new Position(100, 100), t1.getPosition());
        assertEquals(new Size(100, 100), t1.getHeightWidth());
        assertEquals(Continent.RAM, t1.getContinent());

        assertEquals(new Position(200, 100), t2.getPosition());
        assertEquals(new Size(100, 100), t2.getHeightWidth());
        assertEquals(Continent.CPU, t2.getContinent());

        assertEquals(new Position(300, 100), t3.getPosition());
        assertEquals(new Size(100, 100), t3.getHeightWidth());
        assertEquals(Continent.CMOS, t3.getContinent());
    }

    @Test
    public void connectionsCorrectTest() {
        List<Territory> territories = boardLoader.loadTerritories(testBoard);
        HashMap<Integer, Territory> territoriesMap = territoriesToMap(territories);

        Territory t1 =  territoriesMap.get(1);
        Territory t2 =  territoriesMap.get(2);
        Territory t3 =  territoriesMap.get(3);

        assertTrue(t1.getConnections().contains(t2));
        assertTrue(t2.getConnections().contains(t1));
        assertTrue(t2.getConnections().contains(t3));
        assertTrue(t3.getConnections().contains(t2));

        assertFalse(t1.getConnections().contains(t3));
        assertFalse(t3.getConnections().contains(t1));
    }

    @Test
    public void connectionsNoDuplicatesTest() {
        List<Territory> territories = boardLoader.loadTerritories(testBoardDuplicateConn);
        HashMap<Integer, Territory> territoriesMap = territoriesToMap(territories);

        Territory t1 =  territoriesMap.get(1);
        Territory t2 =  territoriesMap.get(2);

        assertEquals(1, t1.getConnections().size());
        assertEquals(1, t2.getConnections().size());
    }


    private HashMap<Integer, Territory> territoriesToMap(List<Territory> territories) {
        HashMap<Integer, Territory> territoriesMap = new HashMap<>();
        for (Territory t : territories) {
            territoriesMap.put(t.getId(), t);
        }
        return territoriesMap;
    }

}
