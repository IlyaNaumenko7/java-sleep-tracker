package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SleepSession {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final SleepQuality quality;

    public SleepSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        this.start = start;
        this.end = end;
        this.quality = quality;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return ChronoUnit.MINUTES.between(start, end);
    }
}