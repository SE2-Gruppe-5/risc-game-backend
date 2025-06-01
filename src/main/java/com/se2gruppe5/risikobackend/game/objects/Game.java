package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.troopterritoryDistribution.AssignTerritories;
import com.se2gruppe5.risikobackend.troopterritoryDistribution.StartTroops;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    @Getter
    private final UUID uuid;
    @Getter
    private List<Territory> territories;
    @Getter
    private final ConcurrentHashMap<UUID, Player> players;

    public List<Player> getPlayerTurnOrder() {
        return playerTurnOrder;
    }

    private final List<Player> playerTurnOrder = new ArrayList<>();

    public Game(UUID uuid, ConcurrentHashMap<UUID, Player> players, List<Territory> territories) {
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

        this.assignTerritories();
        this.distributeStartingTroops(10);
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
}
