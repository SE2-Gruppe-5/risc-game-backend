package com.se2gruppe5.risikobackend.common.objects;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Player(UUID uuid, String name, List<Card> cards, boolean isCurrentTurn) {
    public Player(UUID uuid, String name){
        this(uuid, name, new ArrayList<Card>(), false);
    }
    public int addCard(Card card){
        cards.add(card);
        return cards.size();
    }
    public int removeCard(Card card){
        cards.remove(card);
        return cards.size();
    }
}
