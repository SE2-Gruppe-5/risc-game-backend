package com.se2gruppe5.risikobackend.common.objects;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {
    public Player(UUID uuid, String name){
        this(uuid, name, new ArrayList<>());
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

    public Player(UUID uuid, String name, List<Card> cards){
        this.uuid = uuid;
        this.name = name;
        this.cards = cards;
    }
    private final UUID uuid;
    private final String name;
    private final List<Card> cards;

    private boolean isCurrentTurn = false;
}
