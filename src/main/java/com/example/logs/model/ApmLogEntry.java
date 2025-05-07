package com.example.logs.model;

import java.time.Instant;
import java.util.Map;

public record ApmLogEntry(Instant ts, String metric, double value, Map<String, String> tags)
        implements LogEntry {
    @Override public LogType type() { return LogType.APM; }
}