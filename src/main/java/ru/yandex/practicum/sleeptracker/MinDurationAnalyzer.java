package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MinDurationAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        int minDuration = (int) sessions.stream()
                .mapToLong(SleepSession::getDurationMinutes)
                .min()
                .orElse(0);
        return new SleepAnalysisResult("Minimum sleep duration (minutes)", minDuration);
    }
}
