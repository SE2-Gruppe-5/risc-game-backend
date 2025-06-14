package com.se2gruppe5.risikobackend.common.util.jsonDataclasses;

import com.se2gruppe5.risikobackend.common.objects.Continent;
import com.se2gruppe5.risikobackend.common.objects.helpers.Position;
import com.se2gruppe5.risikobackend.common.objects.helpers.Size;

public class TerritoryJsonData {
    public int id;
    public Continent continent;

    // Not relevant server-side
    // Can be used for server-side map generation
    public Position position;
    public Size heightWidth;
}

