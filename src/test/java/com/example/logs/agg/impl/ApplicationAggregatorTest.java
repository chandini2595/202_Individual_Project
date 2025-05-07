package com.example.logs.agg.impl;

import com.example.logs.model.ApplicationLogEntry;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationAggregatorTest {

    @Test
    void logLevelCounting() {
        var agg = new ApplicationLogAggregator();

        agg.accept(new ApplicationLogEntry(Instant.now(), "ERROR", "Something failed", Map.of()));
        agg.accept(new ApplicationLogEntry(Instant.now(), "INFO", "System ready", Map.of()));
        agg.accept(new ApplicationLogEntry(Instant.now(), "INFO", "Next step", Map.of()));
        agg.accept(new ApplicationLogEntry(Instant.now(), "DEBUG", "Debug message", Map.of()));

        JsonNode snapshot = agg.snapshot();

        assertEquals(1, snapshot.get("ERROR").asInt());
        assertEquals(2, snapshot.get("INFO").asInt());
        assertEquals(1, snapshot.get("DEBUG").asInt());
    }
}