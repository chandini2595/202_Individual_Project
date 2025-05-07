package com.example.logs.parser;

import com.example.logs.model.LogEntry;
import java.util.Optional;

public interface LogParser {
    Optional<LogEntry> parse(String raw);
}