package com.example.logs.agg.impl;

import com.example.logs.agg.LogAggregator;
import com.example.logs.model.LogEntry;
import com.example.logs.model.RequestLogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class RequestLogAggregator implements LogAggregator {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private record LatencyBucket(List<Long> times, Map<String, Integer> status) {}

    private final Map<String, LatencyBucket> map = new HashMap<>();

    @Override
    public void accept(LogEntry e) {
        if (e instanceof RequestLogEntry r) {
            map.computeIfAbsent(r.route(), k -> new LatencyBucket(new ArrayList<>(), new HashMap<>())).times().add(r.latencyMs());
            var st = map.get(r.route()).status();
            String cat = r.status() / 100 + "XX";
            st.merge(cat, 1, Integer::sum);
        }
    }

    @Override
    public ObjectNode snapshot() {
        ObjectNode root = MAPPER.createObjectNode();
        map.forEach((route, b) -> {
            var times = b.times().stream().mapToLong(Long::longValue).sorted().toArray();
            ObjectNode routeNode = root.putObject(route);
            ObjectNode resp = routeNode.putObject("response_times");
            resp.put("min", times[0]);
            resp.put("max", times[times.length - 1]);
            resp.put("50_percentile", percentile(times, 50));
            resp.put("90_percentile", percentile(times, 90));
            resp.put("95_percentile", percentile(times, 95));
            resp.put("99_percentile", percentile(times, 99));
            ObjectNode codes = routeNode.putObject("status_codes");
            b.status().forEach(codes::put);
        });
        return root;
    }

    private long percentile(long[] arr, int pct) {
        int index = (int) Math.ceil(pct / 100.0 * arr.length) - 1;
        return arr[Math.max(index, 0)];
    }

    @Override
    public String outputFile() {
        return "request.json";
    }
}