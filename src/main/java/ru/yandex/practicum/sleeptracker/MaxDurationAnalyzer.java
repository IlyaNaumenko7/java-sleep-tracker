package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxDurationAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        int maxDuration = (int) sessions.stream()
                .mapToLong(SleepSession::getDurationMinutes)
                .max()
                .orElse(0);
        return new SleepAnalysisResult("Maximum sleep duration (minutes)", maxDuration);
    }
}