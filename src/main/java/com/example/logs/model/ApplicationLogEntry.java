package com.example.logs.model;

import java.time.Instant;
import java.util.Map;

public record ApplicationLogEntry(Instant ts, String level, String message, Map<String, String> tags)
        implements LogEntry {
    @Override public LogType type() { return LogType.APPLICATION; }
}