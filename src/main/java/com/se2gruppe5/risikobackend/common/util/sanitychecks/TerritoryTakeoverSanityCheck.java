package com.se2gruppe5.risikobackend.common.util.sanitychecks;

import com.se2gruppe5.risikobackend.common.objects.Territory;
import lombok.Getter;

import java.util.UUID;

// Proof-of-concept, may be fine-tuned or expanded further
public class TerritoryTakeoverSanityCheck {
    @Getter
    private static final TerritoryTakeoverSanityCheck instance = new TerritoryTakeoverSanityCheck();

    private TerritoryTakeoverSanityCheck() {}

    public void plausible(Territory territory, UUID requestedOwner, int requestedStat) {

        // Always allow assignment of a territory if it is currently unowned
        if (territory.getOwner() == null) {

            // If an owner is assigned, troop presence is required
            if(requestedOwner != null && requestedStat > 0) {
                return;
            }

            // If the territory stays unowned, it must not have troops
            if (requestedOwner == null && requestedStat == 0) {
                return;
            }

            throw new IllegalStateException("The territory has an invalid number of troops.");
        }

        // Don't allow clearing an owned territory
        if(requestedOwner == null) {
            throw new IllegalStateException("A territory with an owner cannot be cleared.");
        }

        // Assume a stat change is plausible when the owner stays the same (reinforcement)
        if(territory.getOwner().equals(requestedOwner)) {
            // Troop presence is required
            if (requestedStat > 0) {
                return;
            }
            throw new IllegalStateException("At least one troop must remain on an owned territory.");
        }

        // Check for adjacent attacker and get the attacker max troop count on an adjacent field
        int maxAttackerTroops = 0;
        for(Territory connected : territory.getConnections()) {
            if (connected.getOwner() != null && connected.getOwner().equals(requestedOwner)) {
                maxAttackerTroops = Math.max(maxAttackerTroops, connected.getStat());
            }
        }

        // Assume a stat change is plausible if the attacker has an adjacent troop count matching the stat change
        // Troop presence is required
        if(maxAttackerTroops >= requestedStat && requestedStat > 0) {
            return;
        }

        throw new IllegalStateException("The action is not plausible and was rejected");
    }

}
