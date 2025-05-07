package com.example.logs.agg.impl;

import com.example.logs.model.ApmLogEntry;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApmAggregatorTest {

    @Test
    void medianComputation() {
        ApmLogAggregator agg = new ApmLogAggregator();

        agg.accept(new ApmLogEntry(Instant.now(), "cpu_usage_percent", 60.0, Map.of()));
        agg.accept(new ApmLogEntry(Instant.now(), "cpu_usage_percent", 90.0, Map.of()));
        agg.accept(new ApmLogEntry(Instant.now(), "cpu_usage_percent", 78.0, Map.of()));

        JsonNode snapshot = agg.snapshot();
        JsonNode stats = snapshot.get("cpu_usage_percent");

        assertEquals(60.0, stats.get("minimum").asDouble(), 0.001);
        assertEquals(78.0, stats.get("median").asDouble(), 0.001);
        assertEquals(76.0, stats.get("average").asDouble(), 0.001);
        assertEquals(90.0, stats.get("max").asDouble(), 0.001);
    }
}