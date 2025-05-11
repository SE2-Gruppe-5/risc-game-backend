package com.se2gruppe5.risikobackend.common.objects;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {
    public Player(UUID uuid, String name, int color){
        this(uuid, name, color, new ArrayList<>());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setIsCurrentTurn(boolean b){
        isCurrentTurn = b;
    }

    public String getName() {
        return name;
    }

    public Player(UUID uuid, String name, int color, List<Card> cards){
        this.uuid = uuid;
        this.name = name;
        this.color = color;
        this.cards = cards;
    }
    private final UUID uuid;
    private final String name;
    private final int color;
    private final List<Card> cards;

    private boolean isCurrentTurn = false;

    public int getColor() {
        return color;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isCurrentTurn() {
        return isCurrentTurn;
    }

    public void setCurrentTurn(boolean currentTurn) {
        isCurrentTurn = currentTurn;
    }
}
