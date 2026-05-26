package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

public class SleeplessNightsAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Sleepless nights", 0);
        }

        LocalDate firstDate = sessions.stream()
                .map(SleepSession::getStart)
                .min(LocalDateTime::compareTo)
                .orElseThrow()
                .toLocalDate();

        LocalDate lastDate = sessions.stream()
                .map(SleepSession::getEnd)
                .max(LocalDateTime::compareTo)
                .orElseThrow()
                .toLocalDate();

        long totalNights = ChronoUnit.DAYS.between(firstDate, lastDate) + 1;

        long nightsWithSleep = dateRange(firstDate, lastDate.plusDays(1))
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
        LocalDateTime nightStart = nightDate.atTime(0, 0);
        LocalDateTime nightEnd = nightDate.plusDays(1).atTime(6, 0);

        return sessions.stream()
                .anyMatch(session ->
                        session.getStart().isBefore(nightEnd) &&
                                session.getEnd().isAfter(nightStart)
                );
    }
}