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

        assertTrue(t1.getConnectionIds().contains(t2.getId()));
        assertTrue(t2.getConnectionIds().contains(t1.getId()));
        assertTrue(t2.getConnectionIds().contains(t3.getId()));
        assertTrue(t3.getConnectionIds().contains(t2.getId()));

        assertFalse(t1.getConnectionIds().contains(t3.getId()));
        assertFalse(t3.getConnectionIds().contains(t1.getId()));
    }

    @Test
    public void connectionsNoDuplicatesTest() {
        List<Territory> territories = boardLoader.loadTerritories(testBoardDuplicateConn);
        HashMap<Integer, Territory> territoriesMap = territoriesToMap(territories);

        Territory t1 =  territoriesMap.get(1);
        Territory t2 =  territoriesMap.get(2);

        assertEquals(1, t1.getConnectionIds().size());
        assertEquals(1, t2.getConnectionIds().size());
    }


    private HashMap<Integer, Territory> territoriesToMap(List<Territory> territories) {
        HashMap<Integer, Territory> territoriesMap = new HashMap<>();
        for (Territory t : territories) {
            territoriesMap.put(t.getId(), t);
        }
        return territoriesMap;
    }

}
