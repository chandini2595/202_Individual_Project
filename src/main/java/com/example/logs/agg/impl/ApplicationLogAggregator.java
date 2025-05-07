package com.example.logs.agg.impl;

import com.example.logs.agg.LogAggregator;
import com.example.logs.model.ApplicationLogEntry;
import com.example.logs.model.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class ApplicationLogAggregator implements LogAggregator {
    private final Map<String, Integer> counts = new HashMap<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void accept(LogEntry e) {
        if (e instanceof ApplicationLogEntry a) {
            counts.merge(a.level(), 1, Integer::sum);
        }
    }

    @Override
    public ObjectNode snapshot() {
        ObjectNode root = MAPPER.createObjectNode();
        counts.forEach(root::put);
        return root;
    }

    @Override
    public String outputFile() {
        return "application.json";
    }
}