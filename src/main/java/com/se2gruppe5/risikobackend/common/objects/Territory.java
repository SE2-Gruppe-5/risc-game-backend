package com.se2gruppe5.risikobackend.common.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Territory {
    private UUID owner;
    private int stat;
    private final int id;

    public Territory(UUID owner, int stat, int id) {
        this.owner = owner;
        this.stat = stat;
        this.id = id;
    }
}
