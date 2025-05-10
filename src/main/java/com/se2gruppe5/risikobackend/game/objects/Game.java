package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Card;
import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private Player currentPlayer;
    private UUID uuid;
    private ArrayList<Territory> territories;

    public ConcurrentHashMap<UUID, Player> getPlayers() {
        return players;
    }

    private final ConcurrentHashMap<UUID, Player> players;
    private final List<Player> playerTurnOrder = new ArrayList<>();
    private final int MAX_CARDS = 4;

    public Game(UUID uuid) {
        this(uuid, new ConcurrentHashMap<UUID, Player>(), new ArrayList<Territory>());
    }

    public Game(UUID uuid, ConcurrentHashMap<UUID, Player> players) {
        this(uuid, players, new ArrayList<Territory>());
    }

    public Game(UUID uuid, ConcurrentHashMap<UUID, Player> players, ArrayList<Territory> territories) {
        this.uuid = uuid;
        this.players = players;
        this.territories = territories;
        start();
    }

    private void setAllPlayersCurrentTurnFalse() {
        for (Player player : players.values()) {
            player.setIsCurrentTurn(false);
        }
    }

    public void start() {
        playerTurnOrder.addAll(players.values());
        nextPhase(); //hardcoded at -1, gets +=1 'ed
        nextPlayer(); //hardcoded at -1, gets +=1 'ed

        this.territories = initializeTerritories();
    }

    private ArrayList<Territory> initializeTerritories() {
        return null;
    }

    public void addCardToPlayer(Player player, Card card) {
        if (player.addCard(card) > MAX_CARDS) {
            //send signal to remove card
        }
    }

    public void removeCardFromPlayer(Player player, Card card) {
        player.removeCard(card);
    }

    private int playerIndex = -1;

    public void nextPlayer() {
        setAllPlayersCurrentTurnFalse();
        playerIndex++;
        if (playerIndex >= playerTurnOrder.size()) {
            playerIndex = 0;
        }
        playerTurnOrder.get(playerIndex).setIsCurrentTurn(true);
        requiresPlayerChangeFlag = false;
    }

    private boolean requiresPlayerChangeFlag = false;


    //Phases are: ATTACK, REINFORCE, TRADE
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

    public UUID getUuid() {
        return uuid;
    }

    public void changeTerritory(Territory t) {
        checkTerritoryValid(t);
        territories.remove(getListedTerritoryById(t.id()));
        territories.add(t);
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    private void checkTerritoryValid(Territory t) {
        if (t.id() <= 0) {
            throw new IllegalArgumentException("Territory ID invalid.");
        }
        if (getListedTerritoryById(t.id()) == null) {
            throw new IllegalArgumentException("Territory with ID" + t.id() + "does not exist. [what?] [how?]");
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
    /*
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }



    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<Territory> territories) {
        this.territories = territories;
    }

    public ConcurrentHashMap<UUID, Player> getPlayers() {
        return players;
    }

    public void setPlayers(ConcurrentHashMap<UUID, Player> players) {
        this.players = players;
    }

    public List<UUID> getTurnorder() {
        return turnorder;
    }

    public void setTurnorder(List<UUID> turnorder) {
        this.turnorder = turnorder;
    }

     */
}
