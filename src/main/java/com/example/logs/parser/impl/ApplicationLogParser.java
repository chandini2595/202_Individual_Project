package com.example.logs.parser.impl;

import com.example.logs.model.ApplicationLogEntry;
import com.example.logs.model.LogEntry;
import com.example.logs.parser.LogParser;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class ApplicationLogParser implements LogParser {
    @Override
    public Optional<LogEntry> parse(String raw) {
        if (!raw.contains(" level=")) return Optional.empty();
        Map<String, String> m = KeyValueSplitter.split(raw);
        return Optional.of(new ApplicationLogEntry(
            Instant.parse(m.get("timestamp")),
            m.get("level"),
            m.getOrDefault("message", ""),
            m));
    }
}