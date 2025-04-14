package com.se2gruppe5.risikobackend.common.util;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.function.Predicate;

public class IdUtil {
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();

    public static UUID generateUuid(Predicate<UUID> exists) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (exists.test(uuid));
        return uuid;
    }

    public static String generateId(int length, Predicate<String> exists) {
        String id;
        do {
            id = randomString(length);
        } while (exists.test(id));
        return id;
    }

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET[RANDOM.nextInt(ALPHABET.length)]);
        }
        return sb.toString();
    }
}
