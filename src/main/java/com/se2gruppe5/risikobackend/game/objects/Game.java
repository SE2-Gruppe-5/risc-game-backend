package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.troopterritoryDistribution.AssignTerritories;
import com.se2gruppe5.risikobackend.troopterritoryDistribution.StartTroops;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    @Getter
    private final UUID uuid;
    @Getter
    private ArrayList<Territory> territories;
    @Getter
    private final ConcurrentHashMap<UUID, Player> players;

    public List<Player> getPlayerTurnOrder() {
        return playerTurnOrder;
    }
    private boolean cheatMode = false;


    private final List<Player> playerTurnOrder = new ArrayList<>();

    private final Map<Integer, List<Integer>> territoryNeighbors = initializeTerritoryNeighbors();

    public Game(UUID uuid, ConcurrentHashMap<UUID, Player> players, ArrayList<Territory> territories) {
        this.uuid = uuid;
        this.players = players;
        this.territories = territories;
    }


    private void setAllPlayersCurrentTurnFalse() {
        for (Player player : players.values()) {
            player.setCurrentTurn(false);
        }
    }

    public void start() {
        playerTurnOrder.addAll(players.values());
        nextPhase(); //hardcoded at -1, gets +=1 'ed
        nextPlayer(); //hardcoded at -1, gets +=1 'ed

        this.territories = initializeTerritories();
        this.assignTerritories();
        this.distributeStartingTroops(10);
    }

    private ArrayList<Territory> initializeTerritories() {
        ArrayList<Territory> t = new ArrayList<>(); //todo: implement properly
        t.add(new Territory(playerTurnOrder.getFirst().getId(), 11, 1));
        t.add(new Territory(playerTurnOrder.getLast().getId(), 22, 2));
        return t;
    }

    private int playerIndex = -1;

    public void nextPlayer() {
        setAllPlayersCurrentTurnFalse();
        playerIndex++;
        if (playerIndex >= playerTurnOrder.size()) {
            playerIndex = 0;
        }
        playerTurnOrder.get(playerIndex).setCurrentTurn(true);
        requiresPlayerChangeFlag = false;
    }

    private boolean requiresPlayerChangeFlag = false;


    public int getPhaseIndex() {
        return Math.max(phaseIndex, 0);
    }

    //Phases are: ATTACK, REINFORCE, TRADE
    // (this semantic meaning is in no way conveyed here,
    // as the backend only needs to know about there being three phases ;)
    private int phaseIndex = -1;
    private final int phaseIndexLength = 3;

    public void nextPhase() {
        phaseIndex++;
        if (phaseIndex >= phaseIndexLength) {
            phaseIndex = 0;
            requiresPlayerChangeFlag = true;
        }
    }

    public boolean getRequiresPlayerChange() {
        return requiresPlayerChangeFlag;
    }

    public void changeTerritory(Territory t) {
        checkTerritoryValid(t);
        territories.remove(getListedTerritoryById(t.id()));
        territories.add(t);
    }

    public void updatePlayer(Player p) {
        checkPlayerValid(p);
        players.put(p.getId(), p);
        playerTurnOrder.remove(getListedPlayerById(p.getId()));
        playerTurnOrder.add(p);
    }

    private void checkTerritoryValid(Territory t) {
        if (t.id() <= 0) {
            throw new IllegalArgumentException("Territory ID invalid.");
        }
        if (getListedTerritoryById(t.id()) == null) {
            throw new IllegalArgumentException("Territory with ID" + t.id() + "does not exist. [what?] [how?]");
        }
    }

    private void checkPlayerValid(Player p) {
        if (!players.containsKey(p.getId())) {
            throw new IllegalArgumentException("Territory with ID" + p.getId() + "does not exist. [what?] [how?]");
        }
    }

    private Territory getListedTerritoryById(int id) {
        for (Territory terrs : territories) {
            if (terrs.id() == id) {
                return terrs;
            }
        }
        return null;
    }

    private Player getListedPlayerById(UUID id) {
        for (Player p : players.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    public void enableCheatMode() {
        this.cheatMode = true;
    }

    public boolean isCheatMode() {
        return cheatMode;
    }
    public void cheatConquer(UUID cheatingPlayerId, int targetTerritoryId) {
        if (!isCheatMode()) {
            throw new IllegalStateException("Cheat mode is not enabled.");
        }

        Player cheater = players.get(cheatingPlayerId);
        if (cheater == null) {
            throw new IllegalArgumentException("Player does not exist.");
        }

        Territory target = getListedTerritoryById(targetTerritoryId);
        if (target == null) {
            throw new IllegalArgumentException("Target territory does not exist.");
        }

        // Check if any of the cheater's territories are neighbors to the target
        boolean isNeighbor = territories.stream()
                .filter(t -> cheatingPlayerId.equals(t.owner()))
                .anyMatch(t -> areNeighbors(t, target)); // you need to define this logic

        if (!isNeighbor) {
            throw new IllegalArgumentException("Target is not neighboring any territory owned by the cheating player.");
        }

        // Cheat: Ownership change
        Territory cheated = new Territory(cheatingPlayerId, target.stat(), target.id());
        changeTerritory(cheated);

        System.out.println("[CHEAT] " + cheater.getName() + " instantly conquered territory " + target.id());
    }

    public void assignTerritories() {
        AssignTerritories assigner = new AssignTerritories();

        List<UUID> playerIds = new ArrayList<>(players.keySet());
        List<Integer> territoryIds = new ArrayList<>(territories.stream()
                .map(Territory::id)
                .toList());

        if (territoryIds.size() % playerIds.size() != 0) {
            throw new IllegalStateException("Territories (" + territoryIds.size() + ") must divide evenly among players (" + playerIds.size() + ")");
        }

        // Verteile Territorien zufällig
        Map<UUID, List<Integer>> assigned = assigner.assignTerritories(playerIds, territoryIds);

        for (Map.Entry<UUID, List<Integer>> entry : assigned.entrySet()) {
            UUID playerId = entry.getKey();
            for (Integer territoryId : entry.getValue()) {
                boolean found = false;
                for (Territory t : territories) {
                    if (t.id() == territoryId) {
                        Territory updated = new Territory(playerId, t.stat(), t.id());
                        changeTerritory(updated);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new IllegalStateException("Territory with ID " + territoryId + " not found.");
                }
            }
        }
    }

    public void distributeStartingTroops(int troopsPerPlayer) {
        StartTroops distributor = new StartTroops();

        for (UUID playerId : players.keySet()) {
            List<Territory> owned = territories.stream()
                    .filter(t -> playerId.equals(t.owner()))
                    .toList();

            if (owned.isEmpty()) continue;

            List<Integer> territoryIds = owned.stream()
                    .map(Territory::id)
                    .toList();

            Map<Integer, Integer> distributed = distributor.distributeStartingTroops(territoryIds, troopsPerPlayer);

            for (Territory t : owned) {
                int newStat = distributed.getOrDefault(t.id(), 1);
                Territory updated = new Territory(playerId, newStat, t.id());
                changeTerritory(updated);
            }
        }

    }
    private Map<Integer, List<Integer>> initializeTerritoryNeighbors() {
        Map<Integer, List<Integer>> map = new HashMap<>();

        map.put(1, List.of(2, 9));
        map.put(2, List.of(1, 3, 9));
        map.put(3, List.of(2, 4, 11));
        map.put(4, List.of(3,5,11));
        map.put(5, List.of(4,6,12));
        map.put(6, List.of(5,7));
        map.put(7, List.of(6,8));
        map.put(8, List.of(7,18));
        map.put(9, List.of(1,2,10,20));
        map.put(10, List.of(9,14));
        map.put(11, List.of(3,4,12,15));
        map.put(12, List.of(5,11,16));
        map.put(13, List.of(17,18));
        map.put(14, List.of(10,15,20));
        map.put(15, List.of(11,14,28));
        map.put(16, List.of(12,17,32));
        map.put(17, List.of(13,16));
        map.put(18, List.of(8,13,19));
        map.put(19, List.of(18,35));
        map.put(20, List.of(9,14,21));
        map.put(21, List.of(20,22,26));
        map.put(22, List.of(21,23,46));
        map.put(23, List.of(22,24,27));
        map.put(24, List.of(23,25));
        map.put(25, List.of(24,40,48));
        map.put(26, List.of(21,27,28));
        map.put(27, List.of(23,26,29,40));
        map.put(28, List.of(15,26,29,30));
        map.put(29, List.of(27,28,31,40));
        map.put(30, List.of(28,31,32));
        map.put(31, List.of(29,30,33,42));
        map.put(32, List.of(16,30,33));
        map.put(33, List.of(31,32,34));
        map.put(34, List.of(33,35,37));
        map.put(35, List.of(19,34,36));
        map.put(36, List.of(35,39));
        map.put(37, List.of(34,38));
        map.put(38, List.of(37,39,42,44));
        map.put(39, List.of(36,38,57));
        map.put(40, List.of(25,27,29,41,42));
        map.put(41, List.of(40, 43,48));
        map.put(42, List.of(31,38,40,43));
        map.put(43, List.of(41,42,44,53));
        map.put(44, List.of(38,43,45));
        map.put(45, List.of(44,53,56));
        map.put(46, List.of(22,47,49));
        map.put(47, List.of(46,51));
        map.put(48, List.of(25,41,49));
        map.put(49, List.of(46,48,50));
        map.put(50, List.of(49,52,53));
        map.put(51, List.of(47,52));
        map.put(52, List.of(50,51,60));
        map.put(53, List.of(43,45,50,54,55,56));
        map.put(54, List.of(53,55));
        map.put(55, List.of(53,54));
        map.put(56, List.of(45,53,57,60));
        map.put(57, List.of(39,56,58));
        map.put(58, List.of(57,59));
        map.put(59, List.of(58,60));
        map.put(60, List.of(52,56,59));

        return map;
    }
    public boolean areNeighbors(Territory a, Territory b) {
        List<Integer> neighbors = territoryNeighbors.getOrDefault(a.id(), List.of());
        return neighbors.contains(b.id());
    }
}
