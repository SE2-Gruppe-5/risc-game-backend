package com.se2gruppe5.risikobackend.common.util;


import lombok.Getter;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceFileLoader {
    @Getter
    private static final ResourceFileLoader instance = new ResourceFileLoader();

    private ResourceFileLoader() {}

    public String load(String path) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if(stream == null) {
            throw new IllegalArgumentException("Resource not found or empty: " + path);
        }

        Scanner scanner = new Scanner(stream);
        StringBuilder sb = new StringBuilder();

        while(scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
            if(scanner.hasNextLine()) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
