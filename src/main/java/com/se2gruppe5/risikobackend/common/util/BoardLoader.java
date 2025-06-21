package com.se2gruppe5.risikobackend.common.util;

import com.google.gson.Gson;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.common.util.jsonDataclasses.BoardJsonData;
import com.se2gruppe5.risikobackend.common.util.jsonDataclasses.TerritoryJsonData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoardLoader {
    public List<Territory> loadTerritories(String json) {
        BoardJsonData data = new Gson().fromJson(json, BoardJsonData.class);
        HashMap<Integer, Territory> territoriesMap = new HashMap<>();

        for(TerritoryJsonData t : data.territories()) {
            territoriesMap.put(
                    t.id(), new Territory(t.id(), null, 0, t.continent(), t.position(), t.size())
            );
        }

        for(List<Integer> connection : data.connections()) {
            Territory fromTerritory = territoriesMap.get(connection.getFirst());
            for(int i = 1; i < connection.size(); i++) {
                Territory toTerritory = territoriesMap.get(connection.get(i));

                // Don't add duplicates to connections
                if(!fromTerritory.getConnectionIds().contains(toTerritory.getId())) {
                    fromTerritory.getConnectionIds().add(toTerritory.getId());
                    toTerritory.getConnectionIds().add(fromTerritory.getId());
                }
            }
        }

        return new ArrayList<>(territoriesMap.values());
    }
}
