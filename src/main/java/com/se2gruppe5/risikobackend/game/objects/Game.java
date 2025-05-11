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
    private ArrayList<Territory> territories;
    @Getter
    private final ConcurrentHashMap<UUID, Player> players;
    private final List<Player> playerTurnOrder = new ArrayList<>();

    public Game(UUID uuid) {
        this(uuid, new ConcurrentHashMap<>(), new ArrayList<>());
    }

    public Game(UUID uuid, ConcurrentHashMap<UUID, Player> players) {
        this(uuid, players, new ArrayList<>());
    }

    public Game(UUID uuid, ConcurrentHashMap<UUID, Player> players, ArrayList<Territory> territories) {
        this.uuid = uuid;
        this.players = players;
        this.territories = territories;
        start();
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
    }

    private ArrayList<Territory> initializeTerritories() {
        ArrayList<Territory> t = new ArrayList<>(); //todo: implement properly
        t.add(new Territory(playerTurnOrder.getFirst().getUuid(), 11,1));
        t.add(new Territory(playerTurnOrder.getLast().getUuid(), 22,2));
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


    public int getPhase() {
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
        players.put(p.getUuid(), p);
        playerTurnOrder.remove(getListedPlayerById(p.getUuid()));
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
        if (!players.containsKey(p.getUuid())) {
            throw new IllegalArgumentException("Territory with ID" + p.getUuid() + "does not exist. [what?] [how?]");
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
            if (p.getUuid() == id) {
                return p;
            }
        }
        return null;
    }
    public void assignTerritories(UUID gameId) {
        Game game = getGame(gameId);
        AssignTerritories assigner = new AssignTerritories();

        List<UUID> playerIds = new ArrayList<>(game.getPlayers().keySet());
        List<Integer> territoryIds = game.getTerritories().stream()
                .map(Territory::id)
                .toList();

        if (territoryIds.size() % playerIds.size() != 0) {
            throw new IllegalStateException("Territories must divide evenly among players");
        }

        // Verteile Territorien zufällig
        Map<UUID, List<Integer>> assigned = assigner.assignTerritories(playerIds, territoryIds);

        for (Map.Entry<UUID, List<Integer>> entry : assigned.entrySet()) {
            UUID playerId = entry.getKey();
            for (Integer territoryId : entry.getValue()) {
                for (Territory t : game.getTerritories()) {
                    if (t.id() == territoryId) {
                        Territory updated = new Territory(playerId, t.id(), t.stat());
                        game.changeTerritory(updated);
                        break;
                    }
                }
            }
        }
    }
    public void distributeStartingTroops(UUID gameId, int troopsPerPlayer) {
        Game game = getGame(gameId);
        StartTroops distributor = new StartTroops();

        for (UUID playerId : game.getPlayers().keySet()) {
            List<Territory> owned = game.getTerritories().stream()
                    .filter(t -> playerId.equals(t.owner()))
                    .toList();

            if (owned.isEmpty()) continue;

            List<Integer> territoryIds = owned.stream()
                    .map(Territory::id)
                    .toList();

            Map<Integer, Integer> distributed = distributor.distribute(territoryIds, troopsPerPlayer);

            for (Territory t : owned) {
                int newStat = distributed.getOrDefault(t.id(), 1);
                Territory updated = new Territory(playerId, t.id(), newStat);
                game.changeTerritory(updated);
            }
        }
    }

}
