package com.se2gruppe5.risikobackend.common.util;

import java.util.UUID;
import java.util.function.Predicate;

public class IdUtil {
    public static UUID generateUuid(Predicate<UUID> exists) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (exists.test(uuid));
        return uuid;
    }
}
