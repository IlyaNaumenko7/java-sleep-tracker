package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {
    private List<SleepSession> testSessions;

    @BeforeEach
    void setUp() {
        testSessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD),
                new SleepSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL),
                new SleepSession(LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20), SleepQuality.NORMAL),
                new SleepSession(LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 6, 20), SleepQuality.BAD)
        );
    }

    @Test
    void testTotalSessionsAnalyzer() {
        SleepSessionAnalyzer analyzer = new TotalSessionsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(testSessions);
        assertEquals(4, result.getValue());
    }

    @Test
    void testMinDurationAnalyzer() {
        SleepSessionAnalyzer analyzer = new MinDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(testSessions);
        assertEquals(50, result.getValue());
    }

    @Test
    void testMaxDurationAnalyzer() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0), SleepQuality.GOOD), // 585 мин
                new SleepSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0), SleepQuality.NORMAL), // 540 мин
                new SleepSession(LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20), SleepQuality.NORMAL), // 50 мин
                new SleepSession(LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 6, 20), SleepQuality.BAD) // 410 мин
        );
        SleepSessionAnalyzer analyzer = new MaxDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(585, result.getValue()); // <-- должно быть 585, а не 570
    }

    @Test
    void testAverageDurationAnalyzer() {
        SleepSessionAnalyzer analyzer = new AverageDurationAnalyzer();
        SleepAnalysisResult result = analyzer.apply(testSessions);
        assertTrue((Long) result.getValue() > 0);
    }

    @Test
    void testBadQualitySessionsAnalyzer() {
        SleepSessionAnalyzer analyzer = new BadQualitySessionsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(testSessions);
        assertEquals(1, result.getValue());
    }

    @Test
    void testSleeplessNightsAnalyzer_emptyList() {
        SleepSessionAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(List.of());
        assertEquals(0, result.getValue());
    }

    @Test
    void testSleeplessNightsAnalyzer_consecutiveNights() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 7, 0), SleepQuality.GOOD)
        );
        SleepSessionAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(0, result.getValue());
    }

    @Test
    void testSleeplessNightsAnalyzer_withGap() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepSession(LocalDateTime.of(2025, 10, 4, 23, 0),
                        LocalDateTime.of(2025, 10, 5, 7, 0), SleepQuality.GOOD)
        );
        SleepSessionAnalyzer analyzer = new SleeplessNightsAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertTrue((Integer) result.getValue() >= 1);
    }

    @Test
    void testChronotypeAnalyzer_owl() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 9, 30), SleepQuality.GOOD),
                new SleepSession(LocalDateTime.of(2025, 10, 2, 0, 0),
                        LocalDateTime.of(2025, 10, 2, 10, 0), SleepQuality.GOOD)
        );
        SleepSessionAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.OWL.toString(), result.getValue().toString());
    }

    @Test
    void testChronotypeAnalyzer_lark() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 21, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0), SleepQuality.GOOD),
                new SleepSession(LocalDateTime.of(2025, 10, 2, 20, 0),
                        LocalDateTime.of(2025, 10, 3, 5, 0), SleepQuality.GOOD)
        );
        SleepSessionAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.LARK.toString(), result.getValue().toString());
    }

    @Test
    void testChronotypeAnalyzer_pigeon() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 22, 30),
                        LocalDateTime.of(2025, 10, 2, 7, 0), SleepQuality.GOOD)
        );
        SleepSessionAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.PIGEON.toString(), result.getValue().toString());
    }

    @Test
    void testChronotypeAnalyzer_tie() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 9, 0), SleepQuality.GOOD),
                new SleepSession(LocalDateTime.of(2025, 10, 2, 21, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0), SleepQuality.GOOD)
        );
        SleepSessionAnalyzer analyzer = new ChronotypeAnalyzer();
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.PIGEON.toString(), result.getValue().toString());
    }
}