package com.se2gruppe5.risikobackend.common.util.jsonDataclasses;

import java.util.List;

public record BoardJsonData(
        List<TerritoryJsonData> territories,
        List<List<Integer>> connections) {
}
