package com.example.logs.agg.impl;

import com.example.logs.model.RequestLogEntry;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestAggregatorTest {

    @Test
    void responseStatsPerRoute() {
        var agg = new RequestLogAggregator();

        agg.accept(new RequestLogEntry(Instant.now(), "GET", "/api/data", 200, 100, Map.of()));
        agg.accept(new RequestLogEntry(Instant.now(), "GET", "/api/data", 200, 300, Map.of()));
        agg.accept(new RequestLogEntry(Instant.now(), "GET", "/api/data", 404, 200, Map.of()));
        agg.accept(new RequestLogEntry(Instant.now(), "GET", "/api/data", 500, 500, Map.of()));

        JsonNode snapshot = agg.snapshot();
        JsonNode route = snapshot.get("/api/data");

        assertEquals(100, route.get("response_times").get("min").asLong());
        assertEquals(500, route.get("response_times").get("max").asLong());
        assertEquals(2, route.get("status_codes").get("2XX").asInt());
        assertEquals(1, route.get("status_codes").get("4XX").asInt());
        assertEquals(1, route.get("status_codes").get("5XX").asInt());
    }
}