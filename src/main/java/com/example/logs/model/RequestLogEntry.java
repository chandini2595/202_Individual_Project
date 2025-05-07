package com.example.logs.model;

import java.time.Instant;
import java.util.Map;

public record RequestLogEntry(Instant ts, String method, String route, int status,
                               long latencyMs, Map<String, String> tags)
        implements LogEntry {
    @Override public LogType type() { return LogType.REQUEST; }
}