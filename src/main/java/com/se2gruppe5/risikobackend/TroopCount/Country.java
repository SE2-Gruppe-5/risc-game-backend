package com.se2gruppe5.risikobackend.TroopCount;

public class Country {
    private String name;
    private int troops;

    public Country() {

    }
    public Country(String name, int troops){
        this.name = name;
        this.troops = troops;
}

public String getName() {
        return name;
}

    public void setName(String name) {
        this.name = name;
    }

    public int getTroops() {
        return troops;
    }

    public void setTroops(int troops) {
        this.troops = troops;
    }
}
