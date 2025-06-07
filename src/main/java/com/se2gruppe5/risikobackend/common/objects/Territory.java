package com.se2gruppe5.risikobackend.common.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Territory {
    @Getter
    @Setter
    private UUID owner;

    @Getter
    @Setter
    private int stat;

    @Getter
    private final int id;

    public Territory(UUID owner, int stat, int id) {
        this.owner = owner;
        this.stat = stat;
        this.id = id;
    }
}
