package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AverageDurationAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        long avgDuration = (long) sessions.stream()
                .mapToLong(SleepSession::getDurationMinutes)
                .average()
                .orElse(0);
        return new SleepAnalysisResult("Average sleep duration (minutes)", avgDuration);
    }
}