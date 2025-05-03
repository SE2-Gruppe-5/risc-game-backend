package com.se2gruppe5.risikobackend.TroopCount;

public class TerritoryRecord {
    private String name;
    private String ownerId;
    private int troops;

    // Constructor, Getter, Setter

    public TerritoryRecord(String name, String ownerId, int troops) {
        this.name = name;
        this.ownerId = ownerId;
        this.troops = troops;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public int getTroops() { return troops; }
    public void setTroops(int troops) { this.troops = troops; }
}
