package com.se2gruppe5.risikobackend.game.objects;

import com.se2gruppe5.risikobackend.common.objects.Card;
import com.se2gruppe5.risikobackend.common.objects.Phases;
import com.se2gruppe5.risikobackend.common.objects.Player;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private Player currentPlayer;
    private UUID uuid;
    private ArrayList<Territory> territories;
    private ConcurrentHashMap<UUID, Player> players;
    private List<UUID> turnorder = new ArrayList<>();
    private Phases phase = Phases.Reinforce;
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

    public void start() {
        for(Player player : players.values()) {
            turnorder.add(player.uuid());
        }
        currentPlayer = players.get(turnorder.getFirst());
        this.territories = initializeTerritories();
    }

    private ArrayList<Territory> initializeTerritories() {
        return null;
    }

    public void addCardToPlayer(Player player, Card card) {
        if(player.addCard(card) >MAX_CARDS){
            //send signal to remove card
        }
    }
    public void removeCardFromPlayer(Player player, Card card) {
        player.removeCard(card);
    }
    public void nextPlayer(){
        int i = 0;
        while(currentPlayer != players.get(turnorder.get(i))) {
            i++;
        }
        if(i == turnorder.size()-1){
            currentPlayer = players.get(turnorder.getFirst());
        }else currentPlayer = players.get(turnorder.get(i));
    }
    public void nextPhase(){
        switch(phase){
            case Reinforce:{
                phase = Phases.ATTACK;
            }
                break;
            case ATTACK:{
                phase = Phases.TRADE;
            }
                break;
            case TRADE:{
                phase = Phases.Reinforce;
                nextPlayer();
            }
                break;
        }
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public UUID getUuid() {
        return uuid;
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
}
