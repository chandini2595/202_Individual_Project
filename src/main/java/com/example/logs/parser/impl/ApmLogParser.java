package com.example.logs.parser.impl;

import com.example.logs.model.ApmLogEntry;
import com.example.logs.model.LogEntry;
import com.example.logs.parser.LogParser;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class ApmLogParser implements LogParser {
    @Override
    public Optional<LogEntry> parse(String raw) {
        if (!raw.contains(" metric=")) return Optional.empty();
        Map<String, String> map = KeyValueSplitter.split(raw);
        try {
            return Optional.of(new ApmLogEntry(
                Instant.parse(map.get("timestamp")),
                map.get("metric"),
                Double.parseDouble(map.get("value")),
                map));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}