package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionsAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        return new SleepAnalysisResult("Total sleep sessions", sessions.size());
    }
}
