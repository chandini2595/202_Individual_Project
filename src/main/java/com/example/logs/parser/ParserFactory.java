package com.example.logs.parser;

import com.example.logs.parser.impl.*;

import java.util.List;

public class ParserFactory {
    public static List<LogParser> all() {
        return List.of(
            new ApmLogParser(),
            new ApplicationLogParser(),
            new RequestLogParser()
        );
    }
}