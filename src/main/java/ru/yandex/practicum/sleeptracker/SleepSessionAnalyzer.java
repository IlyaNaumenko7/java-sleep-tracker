package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface SleepSessionAnalyzer extends Function<List<SleepSession>, SleepAnalysisResult> {
}