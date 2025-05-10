package com.se2gruppe5.risikobackend.common.objects;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record Player(UUID uuid, String name, List<Card> cards) {
    public Player(UUID uuid, String name){
        this(uuid, name, new ArrayList<Card>());
    }


}
