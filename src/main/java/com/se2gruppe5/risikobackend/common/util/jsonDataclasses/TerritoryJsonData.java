package com.se2gruppe5.risikobackend.common.util.jsonDataclasses;

public class TerritoryJsonData {
    public int id;

    // These properties are currently not used but may be relevant if we want to e.g. configure the map server-side
    public String continent;
    public Position position;
    public Size size;

    public record Position(int x, int y) {}
    public record Size(int x, int y) {}
}

