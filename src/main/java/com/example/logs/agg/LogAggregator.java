package com.example.logs.agg;

import com.example.logs.model.LogEntry;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface LogAggregator {
    void accept(LogEntry entry);
    ObjectNode snapshot();
    String outputFile();
}
