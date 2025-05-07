package com.se2gruppe5.risikobackend.TroopCount;

public class TerritoryRecord {
    private int id;
    private String owner;
    private int troops;

    // Constructor, Getter, Setter

    public TerritoryRecord(int id, String owner, int troops) {
        this.id = id;
        this.owner = owner;
        this.troops = troops;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public int getTroops() { return troops; }
    public void setTroops(int troops) { this.troops = troops; }

    }
