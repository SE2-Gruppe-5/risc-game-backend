package com.se2gruppe5.risikobackend.TroopCount;

public class TerritoryRecord {
    private int id;
    private String ownerId;
    private int troops;

    // Constructor, Getter, Setter

    public TerritoryRecord(int id, String ownerId, int troops) {
        this.id = id;
        this.ownerId = ownerId;
        this.troops = troops;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public int getTroops() { return troops; }
    public void setTroops(int troops) { this.troops = troops; }

    }
