package com.se2gruppe5.risikobackend.common.util.jsondata;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.helpers.Position;
import com.se2gruppe5.risikobackend.common.objects.helpers.Size;

public record TerritoryJsonData(
    int id,
    Continent continent,

    // Not relevant server-side
    // Can be used for server-side map generation
    Position position,
    Size size) {
}
