package com.se2gruppe5.risikobackend.common.objects;

import com.se2gruppe5.risikobackend.common.objects.helpers.Position;
import com.se2gruppe5.risikobackend.common.objects.helpers.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
public class Territory {
    private UUID owner;
    private int stat;
    private final int id;
    private final Continent continent;
    private final ArrayList<Territory> connections =  new ArrayList<>();
    private final Position position;
    private final Size heightWidth;

    public boolean isConnected(Territory territory) {
        return connections.contains(territory);
    }

    // Minimal constructor e.g. for unit tests
    public Territory(int id, UUID owner, int stat, Continent continent) {
        this(
                id,
                owner,
                stat,
                continent,
                new Position(0, 0),
                new Size(0, 0)
        );
    }

    public Territory(int id, UUID owner, int stat, Continent continent, Position position, Size heightWidth) {
        this.id = id;
        this.owner = owner;
        this.stat = stat;
        this.continent = continent;
        this.position = position;
        this.heightWidth = heightWidth;
    }
}
