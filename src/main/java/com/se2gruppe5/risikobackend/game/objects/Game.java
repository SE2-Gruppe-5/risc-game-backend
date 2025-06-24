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
        this.distributeStartingTroops((int)Math.ceil( (double)territories.size() / players.size() ));
    }

    private int playerIndex = -1;

    public List<Player> getAlivePlayers() {
        return playerTurnOrder.stream()
                .filter(p -> !p.isDead())
                .toList();
    }

    public void nextPlayer() {
        if (getAlivePlayers().size() < 2) {
            requiresPlayerChangeFlag = false;
            return; //prevent deadlock
        }
        setAllPlayersCurrentTurnFalse();
        do {
            playerIndex++;
            if (playerIndex >= playerTurnOrder.size()) {
                playerIndex = 0;
            }
        } while (playerTurnOrder.get(playerIndex).isDead());
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

    public Territory getTerritoryById(int id) {
        for (Territory terrs : territories) {
            if (terrs.getId() == id) {
                return terrs;
            }
        }
        throw new IllegalArgumentException("Territory ID invalid.");
    }

    public Player getPlayerById(UUID id) {
        for (Player p : players.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("Player ID invalid.");
    }

    public void assignTerritories() {
        AssignTerritories assigner = new AssignTerritories();

        List<UUID> playerIds = new ArrayList<>(players.keySet());
        List<Integer> territoryIds = new ArrayList<>(territories.stream()
                .map(Territory::getId)
                .toList());

        if (territoryIds.size() % playerIds.size() != 0) {
            throw new IllegalStateException("Territories (" + territoryIds.size() + ") must divide evenly among players (" + playerIds.size() + ")");
        }

        // Verteile Territorien zufällig
        Map<UUID, List<Integer>> assigned = assigner.assignTerritories(playerIds, territoryIds);

        for (Map.Entry<UUID, List<Integer>> entry : assigned.entrySet()) {
            UUID playerId = entry.getKey();
            for (Integer territoryId : entry.getValue()) {
                getTerritoryById(territoryId).setOwner(playerId);
            }
        }
    }

    public void distributeStartingTroops(int troopsPerPlayer) {
        StartTroops distributor = new StartTroops();

        for (UUID playerId : players.keySet()) {
            List<Territory> owned = territories.stream()
                    .filter(t -> playerId.equals(t.getOwner()))
                    .toList();

            if (owned.isEmpty()) continue;

            List<Integer> territoryIds = owned.stream()
                    .map(Territory::getId)
                    .toList();

            Map<Integer, Integer> distributed = distributor.distributeStartingTroops(territoryIds, troopsPerPlayer);

            for (Territory t : owned) {
                int newStat = distributed.getOrDefault(t.getId(), 1);
                Territory territory = getTerritoryById(t.getId());
                territory.setOwner(playerId);
                territory.setStat(newStat);
            }
        }

    }

    public UUID checkWon() {

        for(Player player : players.values()){
            if (player.isDead()) {
                continue;
            }

            block: {
                for(Territory territory : territories){
                    if (territory.getOwner().equals(player.getId())) {
                        break block;
                    }
                }
                player.setDead(true);
            }
        }

        Player alivePlayer = null;
        for (Player player : players.values()) {
            if (!player.isDead()) {
                if (alivePlayer != null) {
                    // More than one player is still alive
                    return null;
                }
                alivePlayer = player;
            }
        }

        return alivePlayer == null ? null : alivePlayer.getId();

    }
}
