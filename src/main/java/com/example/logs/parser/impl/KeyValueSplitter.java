package com.example.logs.parser.impl;

import java.util.HashMap;
import java.util.Map;

public class KeyValueSplitter {
    public static Map<String, String> split(String line) {
        Map<String, String> map = new HashMap<>();
        for (String token : line.split("\\s+")) {
            int idx = token.indexOf('=');
            if (idx > 0) {
                map.put(token.substring(0, idx), token.substring(idx + 1));
            }
        }
        return map;
    }
}