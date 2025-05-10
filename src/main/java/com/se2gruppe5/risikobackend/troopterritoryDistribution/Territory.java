package com.se2gruppe5.risikobackend.troopterritoryDistribution;

import java.util.UUID;

public class Territory {
    private final String id;
    private int stat;  // Anzahl der Truppen
    private UUID owner;  // Der Besitzer des Territoriums (UUID des Spielers)

    public Territory(String id, int stat) {
        this.id = id;
        this.stat = stat;
        this.owner = null;
    }

    public String getId() {
        return id;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }
}
