package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

public class SleeplessNightsAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Sleepless nights", 0);
        }

        LocalDateTime firstStart = sessions.stream()
                .map(SleepSession::getStart)
                .min(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDateTime lastEnd = sessions.stream()
                .map(SleepSession::getEnd)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        // Определяем первую ночь по правилу: до 12:00 — предыдущая, после — следующая
        LocalDate firstNight = (firstStart.toLocalTime().isBefore(LocalTime.of(12, 0)))
                ? firstStart.toLocalDate()
                : firstStart.toLocalDate().plusDays(1);

        // Определяем последнюю ночь по тому же правилу
        LocalDate lastNight = (lastEnd.toLocalTime().isBefore(LocalTime.of(12, 0)))
                ? lastEnd.toLocalDate()
                : lastEnd.toLocalDate().plusDays(1);

        long totalNights = ChronoUnit.DAYS.between(firstNight, lastNight) + 1;

        long nightsWithSleep = dateRange(firstNight, lastNight.plusDays(1))
                .filter(nightDate -> hasSleepDuringNight(sessions, nightDate))
                .count();

        int sleeplessNights = (int) (totalNights - nightsWithSleep);
        return new SleepAnalysisResult("Sleepless nights", sleeplessNights);
    }

    private Stream<LocalDate> dateRange(LocalDate start, LocalDate endExclusive) {
        long days = ChronoUnit.DAYS.between(start, endExclusive);
        return Stream.iterate(start, date -> date.plusDays(1))
                .limit(days);
    }

    private boolean hasSleepDuringNight(List<SleepSession> sessions, LocalDate nightDate) {
        // Интервал ночи: [nightDate 00:00; nightDate 06:00) — строго один день!
        LocalDateTime nightStart = nightDate.atTime(0, 0);
        LocalDateTime nightEnd = nightDate.atTime(6, 0);

        return sessions.stream()
                .anyMatch(session ->
                        session.getStart().isBefore(nightEnd) &&
                                session.getEnd().isAfter(nightStart)
                );
    }
}