package com.se2gruppe5.risikobackend.common.objects;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Player {
    public Player(UUID uuid, String name, int color){
        this(uuid, name, color, new ArrayList<>());
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
}
