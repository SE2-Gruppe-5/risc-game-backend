package com.se2gruppe5.risikobackend.common.util;

import com.google.gson.Gson;
import com.se2gruppe5.risikobackend.common.objects.Territory;
import com.se2gruppe5.risikobackend.common.util.jsonDataclasses.BoardJsonData;
import com.se2gruppe5.risikobackend.common.util.jsonDataclasses.TerritoryJsonData;

import java.util.ArrayList;
import java.util.List;

public class BoardLoader {
    public static List<Territory> loadTerritories(String json) {
        BoardJsonData data = new Gson().fromJson(json, BoardJsonData.class);
        ArrayList<Territory> territories = new ArrayList<>();

        for(TerritoryJsonData territory : data.territories) {
            territories.add(new Territory(null, 0, territory.id));
        }

        return territories;
    }
}
