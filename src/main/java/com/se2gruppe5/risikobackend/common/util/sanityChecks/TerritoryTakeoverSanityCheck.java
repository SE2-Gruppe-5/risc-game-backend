package com.se2gruppe5.risikobackend.common.util.sanityChecks;

import com.se2gruppe5.risikobackend.common.objects.Territory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

// Proof-of-concept, may be fine-tuned or expanded further
public class TerritoryTakeoverSanityCheck {

    public void plausible(Territory territory, List<Territory> territories, UUID requestedOwner, int requestedStat) {
        // Create hash map out of territory list for easier access
        HashMap<Integer, Territory> territoryMap = new HashMap<>();
        for (Territory t : territories) {
            territoryMap.put(t.getId(), t);
        }

        // Assume any stat change is plausible when the owner stays the same (reinforcement)
        if (territory.getOwner() == null || (requestedOwner == null && requestedStat == 0) ||
                territory.getOwner().equals(requestedOwner)) {
            return;
        }

        // Assume a stat change is plausible if the attacker has an adjacent troop count matching the stat change
        boolean adjacentAttacker = false;
        int maxAttackerTroops = 0;
        for(int connectedId : territory.getConnectionIds()) {
            Territory connected = territoryMap.get(connectedId);
            if (connected.getOwner().equals(requestedOwner)) {
                adjacentAttacker = true;
                maxAttackerTroops = Math.max(maxAttackerTroops, connected.getStat());
            }
        }

        if(adjacentAttacker && maxAttackerTroops >= requestedStat) {
            return;
        }

        throw new IllegalStateException("The action is not plausible and was rejected");
    }

}
