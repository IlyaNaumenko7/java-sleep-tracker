package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualitySessionsAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        int badCount = (int) sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Bad quality sleep sessions", badCount);
    }
}