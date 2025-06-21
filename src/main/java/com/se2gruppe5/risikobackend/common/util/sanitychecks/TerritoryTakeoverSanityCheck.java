package com.se2gruppe5.risikobackend.common.util.sanitychecks;

import com.se2gruppe5.risikobackend.common.objects.Territory;

import java.util.UUID;

// Proof-of-concept, may be fine-tuned or expanded further
public class TerritoryTakeoverSanityCheck {

    public void plausible(Territory territory, UUID requestedOwner, int requestedStat) {
        // Always allow assignment of a territory if it is currently unowned
        if (territory.getOwner() == null) {
            return;
        }

        // Always allow clearing a territory assignment
        if(requestedOwner == null) {
            if(requestedStat == 0) {
                return;
            }
            else {
                throw new IllegalStateException("Ownerless territory cannot have troops");
            }
        }

        // Assume any stat change is plausible when the owner stays the same (reinforcement)
        if(territory.getOwner().equals(requestedOwner)) {
            return;
        }

        boolean adjacentAttacker = false;
        int maxAttackerTroops = 0;
        for(Territory connected : territory.getConnections()) {
            if (connected.getOwner() != null && connected.getOwner().equals(requestedOwner)) {
                adjacentAttacker = true;
                maxAttackerTroops = Math.max(maxAttackerTroops, connected.getStat());
            }
        }

        // Assume a stat change is plausible if the attacker has an adjacent troop count matching the stat change
        if(adjacentAttacker && maxAttackerTroops >= requestedStat) {
            return;
        }

        throw new IllegalStateException("The action is not plausible and was rejected");
    }

}
