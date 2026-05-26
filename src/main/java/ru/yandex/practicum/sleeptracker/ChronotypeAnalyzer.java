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

        // Фильтр: учитываем только сессии, пересекающие полночь
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

    // Ночная сессия: началась в один день, закончилась в другой
    private boolean isNightSession(SleepSession session) {
        return !session.getStart().toLocalDate().equals(session.getEnd().toLocalDate());
    }

    private Chronotype getChronotypeForSession(SleepSession session) {
        LocalTime start = session.getStart().toLocalTime();
        LocalTime end = session.getEnd().toLocalTime();

        // Сова: засыпание >= 23:00 И пробуждение >= 9:00
        boolean isOwlStart = !start.isBefore(LocalTime.of(23, 0));
        boolean isOwlEnd = !end.isBefore(LocalTime.of(9, 0));
        if (isOwlStart && isOwlEnd) {
            return Chronotype.OWL;
        }

        // Жаворонок: засыпание < 22:00 И пробуждение < 7:00
        boolean isLarkStart = start.isBefore(LocalTime.of(22, 0));
        boolean isLarkEnd = end.isBefore(LocalTime.of(7, 0));
        if (isLarkStart && isLarkEnd) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }

    private Chronotype determineUserChronotype(Map<Chronotype, Long> counts) {
        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        // При равенстве или неопределённости — Голубь
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