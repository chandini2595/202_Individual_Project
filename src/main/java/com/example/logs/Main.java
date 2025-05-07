package com.example.logs;

import com.example.logs.agg.LogAggregator;
import com.example.logs.agg.impl.*;
import com.example.logs.model.LogEntry;
import com.example.logs.parser.LogParser;
import com.example.logs.parser.ParserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class Main {
    private static final List<LogAggregator> aggregators = List.of(
        new ApmLogAggregator(), new ApplicationLogAggregator(), new RequestLogAggregator()
    );

    public static void main(String[] args) throws IOException {
        if (args.length != 2 || !"--file".equals(args[0])) {
            System.err.println("Usage: java -jar log-aggregator.jar --file <input.txt>");
            return;
        }

        String filePath = args[1];
        List<LogParser> parsers = ParserFactory.all();

        try (BufferedReader br = Files.newBufferedReader(Path.of(filePath))) {
            br.lines().forEach(line -> {
                for (LogParser parser : parsers) {
                    parser.parse(line).ifPresent(entry -> {
                        for (LogAggregator agg : aggregators) {
                            agg.accept(entry);
                        }
                    });
                }
            });
        }

ObjectMapper mapper = new ObjectMapper();

for (LogAggregator agg : aggregators) {
    Path outFile = Paths.get(agg.outputFile());
    Files.writeString(outFile,
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(agg.snapshot()));
}
    }
}