package com.example.logs.parser.impl;

import com.example.logs.model.LogEntry;
import com.example.logs.model.RequestLogEntry;
import com.example.logs.parser.LogParser;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class RequestLogParser implements LogParser {
    @Override
    public Optional<LogEntry> parse(String raw) {
        if (!raw.contains(" request_method=")) return Optional.empty();
        Map<String, String> m = KeyValueSplitter.split(raw);
        try {
            return Optional.of(new RequestLogEntry(
                Instant.parse(m.get("timestamp")),
                m.get("request_method"),
                unquote(m.get("request_url")),
                Integer.parseInt(m.get("response_status")),
                Long.parseLong(m.get("response_time_ms")),
                m));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static String unquote(String s) {
        return s == null ? null : s.replaceAll("^\"|\"$", "");
    }
}