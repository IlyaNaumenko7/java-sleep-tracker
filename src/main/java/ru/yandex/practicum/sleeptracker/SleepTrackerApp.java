package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerApp {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private final List<SleepSessionAnalyzer> analyzers = new ArrayList<>();
    private final String filePath;

    public SleepTrackerApp(String filePath) {
        this.filePath = filePath;
        initializeAnalyzers();
    }

    private void initializeAnalyzers() {
        analyzers.add(new TotalSessionsAnalyzer());
        analyzers.add(new MinDurationAnalyzer());
        analyzers.add(new MaxDurationAnalyzer());
        analyzers.add(new AverageDurationAnalyzer());
        analyzers.add(new BadQualitySessionsAnalyzer());
        analyzers.add(new SleeplessNightsAnalyzer());
        analyzers.add(new ChronotypeAnalyzer());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java SleepTrackerApp <path_to_sleep_log>");
            return;
        }

        try {
            SleepTrackerApp app = new SleepTrackerApp(args[0]);
            List<SleepSession> sessions = app.loadSleepSessions();
            List<SleepAnalysisResult> results = app.analyze(sessions);
            results.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private List<SleepSession> loadSleepSessions() throws IOException {
        List<SleepSession> sessions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(line -> line.split(";"))
                    .map(parts -> new SleepSession(
                            LocalDateTime.parse(parts[0].trim(), FORMATTER),
                            LocalDateTime.parse(parts[1].trim(), FORMATTER),
                            SleepQuality.valueOf(parts[2].trim())
                    ))
                    .forEach(sessions::add);
        }
        return sessions;
    }

    private List<SleepAnalysisResult> analyze(List<SleepSession> sessions) {
        return analyzers.stream()
                .map(analyzer -> analyzer.apply(sessions))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}