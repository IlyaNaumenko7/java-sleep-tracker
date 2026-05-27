package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChronotypeAnalyzer implements SleepSessionAnalyzer {
    @Override
    public SleepAnalysisResult apply(List<SleepSession> sessions) {
        Map<Chronotype, Long> counts = new HashMap<>();
        counts.put(Chronotype.OWL, 0L);
        counts.put(Chronotype.LARK, 0L);
        counts.put(Chronotype.PIGEON, 0L);

        Map<Chronotype, Long> result = sessions.stream()
                .filter(this::isNightSession)
                .map(this::getChronotypeForSession)
                .collect(java.util.stream.Collectors.groupingBy(
                        type -> type,
                        java.util.stream.Collectors.counting()
                ));

        counts.putAll(result);
        Chronotype userChronotype = determineUserChronotype(counts);
        return new SleepAnalysisResult("User chronotype", userChronotype);
    }

    private boolean isNightSession(SleepSession session) {
        return !session.getStart().toLocalDate().equals(session.getEnd().toLocalDate());
    }

    private Chronotype getChronotypeForSession(SleepSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        // Сова: засыпание СТРОГО ПОСЛЕ 23:00 И пробуждение СТРОГО ПОСЛЕ 9:00
        if (start.isAfter(LocalTime.of(23, 0)) && end.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        }

        // Жаворонок: засыпание ДО 22:00 И пробуждение ДО 7:00 (строгие)
        if (start.isBefore(LocalTime.of(22, 0)) && end.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }

    private Chronotype determineUserChronotype(Map<Chronotype, Long> counts) {
        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        if (pigeonCount >= owlCount && pigeonCount >= larkCount) {
            return Chronotype.PIGEON;
        }
        if (owlCount > larkCount) {
            return Chronotype.OWL;
        }
        if (larkCount > owlCount) {
            return Chronotype.LARK;
        }
        return Chronotype.PIGEON;
    }
}