package com.example.logs.agg.impl;

import com.example.logs.agg.LogAggregator;
import com.example.logs.model.ApmLogEntry;
import com.example.logs.model.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class ApmLogAggregator implements LogAggregator {
    private final Map<String, List<Double>> buckets = new HashMap<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void accept(LogEntry e) {
        if (e instanceof ApmLogEntry a) {
            buckets.computeIfAbsent(a.metric(), k -> new ArrayList<>()).add(a.value());
        }
    }

    @Override
    public ObjectNode snapshot() {
        ObjectNode root = MAPPER.createObjectNode();
        buckets.forEach((metric, values) -> {
            Double[] arr = values.toArray(Double[]::new);
            Arrays.sort(arr);
            ObjectNode stats = root.putObject(metric);
            stats.put("minimum", arr[0]);
            stats.put("median", median(arr));
            stats.put("average", Arrays.stream(arr).mapToDouble(Double::doubleValue).average().orElse(0));
            stats.put("max", arr[arr.length - 1]);
        });
        return root;
    }

    private static double median(Double[] v) {
        int n = v.length;
        return (n % 2 == 0) ? (v[n/2 - 1] + v[n/2]) / 2.0 : v[n/2];
    }

    @Override
    public String outputFile() {
        return "apm.json";
    }
}