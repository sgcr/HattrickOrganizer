package core.util;

import core.model.TranslationFacility;
import core.model.Translator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class HODurationTest {

    private static final long DAYS_PER_WEEK = 7L;
    private static final long WEEKS_PER_SEASON = 16L;
    private static final long DAYS_PER_SEASON = DAYS_PER_WEEK * WEEKS_PER_SEASON;

    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long MINUTES_PER_HOUR = 60L;
    private static final long SECONDS_PER_HOUR = MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
    private static final long HOURS_PER_DAY = 24L;
    private static final long SECONDS_PER_DAY = HOURS_PER_DAY * SECONDS_PER_HOUR;
    private static final long SECONDS_PER_SEASON = DAYS_PER_SEASON * SECONDS_PER_DAY;
    private static final long SECONDS_PER_WEEK = DAYS_PER_WEEK * SECONDS_PER_DAY;

    private static Stream<Arguments> differentDurations() {
        return Stream.of(
            of(new HODuration(0, 0), new HODuration(0, 1)),
            of(new HODuration(1, 0), new HODuration(0, 111)),
            of(new HODuration(1, 5), new HODuration(1, 6)),
            of(new HODuration(1, 0), new HODuration(2, 0)),
            of(new HODuration(-1, 111), new HODuration(0, 0))
        );
    }

    private static Stream<Arguments> equalDurations() {
        return Stream.of(
            of(new HODuration(0, 0), new HODuration(0, 0)),
            of(new HODuration(1, 5), new HODuration(1, 5)),
            of(new HODuration(0, 112), new HODuration(1, 0)),
            of(new HODuration(0, 113), new HODuration(1, 1)),
            of(new HODuration(2, 224), new HODuration(4, 0)),
            of(new HODuration(0, -1), new HODuration(-1, 111)),
            of(new HODuration(0, -112), new HODuration(-1, 0)),
            of(new HODuration(0, -113), new HODuration(-2, 111))
        );
    }

    @Test
    void defaultCtor() {
        final var hoDuration = new HODuration();
        assertThat(hoDuration.getTotalSeconds()).isEqualTo(0);
        assertThat(hoDuration.getSeasons()).isEqualTo(0);
        assertThat(hoDuration.getWeeks()).isEqualTo(0);
        assertThat(hoDuration.getDays()).isEqualTo(0);
        assertThat(hoDuration.getHours()).isEqualTo(0);
        assertThat(hoDuration.getMinutes()).isEqualTo(0);
        assertThat(hoDuration.getSeconds()).isEqualTo(0);
        assertThat(hoDuration.getDaysInSeason()).isEqualTo(0);
    }

    private static Stream<Arguments> ctor_seasonsAndDays() {
        return Stream.of(
            of(1, 1, 1, 1),
            of(0, DAYS_PER_SEASON - 1, 0, DAYS_PER_SEASON - 1),
            of(0, DAYS_PER_SEASON, 1, 0),
            of(0, DAYS_PER_SEASON + 10, 1, 10),
            of(0, DAYS_PER_SEASON * 2 + 111, 2, 111),
            of(1, DAYS_PER_SEASON - 1, 1, DAYS_PER_SEASON - 1),
            of(2, DAYS_PER_SEASON, 3, 0),
            of(3, DAYS_PER_SEASON + 10, 4, 10),
            of(4, DAYS_PER_SEASON * 2 + 111, 6, 111),
            of(0, -1, -1, 111),
            of(-1, 0, -1, 0),
            of(-1, -1, -2, 111),
            of(-1, DAYS_PER_SEASON, 0, 0),
            of(0, 0, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource
    void ctor_seasonsAndDays(long seasons, long days, long expectedSeasons, long expectedDays) {
        final var hoDuration = new HODuration(seasons, days);
        assertThat(hoDuration.getSeasons()).isEqualTo(expectedSeasons);
        assertThat(hoDuration.getDaysInSeason()).isEqualTo(expectedDays);
    }

    private static Stream<Arguments> between() {
        return Stream.of(
            // same time
            of("2026-01-01T00:00:00Z", "2026-01-01T00:00:00Z", new HODuration(0, 0)),
            // below the rounding threshold
            of("2026-01-01T00:00:00Z", "2026-01-01T11:59:59Z", new HODuration(0, 0)),
            // exactly 12 hours -> 1 day
            of("2026-01-01T00:00:00Z", "2026-01-01T12:00:00Z", new HODuration(0, 1)),
            // just below 1.5 days -> 1 day
            of("2026-01-01T00:00:00Z", "2026-01-02T11:59:59Z", new HODuration(0, 1)),
            // exactly 1.5 days -> 2 days
            of("2026-01-01T00:00:00Z", "2026-01-02T12:00:00Z", new HODuration(0, 2)),
            // 111.5 days -> 112 days -> 1 season
            of("2026-01-01T00:00:00Z", "2026-04-22T12:00:00Z", new HODuration(1, 0))
        );
    }

    private static Stream<Arguments> fromSeconds() {
        return Stream.of(
            of(0L),
            of(1L),
            of(SECONDS_PER_MINUTE),
            of(SECONDS_PER_HOUR),
            of(SECONDS_PER_DAY),
            of(SECONDS_PER_WEEK),
            of(SECONDS_PER_SEASON),
            of(SECONDS_PER_SEASON + 1),
            of(-1L),
            of(-SECONDS_PER_MINUTE),
            of(-SECONDS_PER_DAY),
            of(-SECONDS_PER_WEEK),
            of(-SECONDS_PER_SEASON),
            of(Long.MAX_VALUE),
            of(Long.MIN_VALUE)
        );
    }

    @ParameterizedTest
    @MethodSource
    void fromSeconds(long totalSeconds) {
        final var hoDuration = HODuration.fromSeconds(totalSeconds);
        assertThat(hoDuration.getTotalSeconds()).isEqualTo(totalSeconds);
    }

    private static Stream<Arguments> durationParts() {
        return Stream.of(
            of(0L, 0, 0, 0, 0, 0, 0),

            of(1L, 0, 0, 0, 0, 0, 1),
            of(SECONDS_PER_MINUTE, 0, 0, 0, 0, 1, 0),
            of(SECONDS_PER_HOUR, 0, 0, 0, 1, 0, 0),
            of(SECONDS_PER_DAY, 0, 0, 1, 0, 0, 0),
            of(SECONDS_PER_WEEK, 0, 1, 0, 0, 0, 0),

            of(
                2 * SECONDS_PER_WEEK
                    + 3 * SECONDS_PER_DAY
                    + 4 * SECONDS_PER_HOUR
                    + 5 * SECONDS_PER_MINUTE
                    + 6,
                0, 2, 3, 4, 5, 6
            ),

            of(SECONDS_PER_SEASON - 1, 0, 15, 6, 23, 59, 59),
            of(SECONDS_PER_SEASON, 1, 0, 0, 0, 0, 0),
            of(SECONDS_PER_SEASON + 1, 1, 0, 0, 0, 0, 1),

            of(
                2 * SECONDS_PER_SEASON
                    + 3 * SECONDS_PER_WEEK
                    + 4 * SECONDS_PER_DAY
                    + 5 * SECONDS_PER_HOUR
                    + 6 * SECONDS_PER_MINUTE
                    + 7,
                2, 3, 4, 5, 6, 7
            ),

            of(-1L, -1, 15, 6, 23, 59, 59),
            of(-SECONDS_PER_WEEK, -1, 15, 0, 0, 0, 0),
            of(-SECONDS_PER_SEASON, -1, 0, 0, 0, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource
    void durationParts(
        long totalSeconds,
        long expectedSeasons,
        long expectedWeeks,
        long expectedDays,
        long expectedHours,
        long expectedMinutes,
        long expectedSeconds
    ) {
        final var hoDuration = HODuration.fromSeconds(totalSeconds);

        assertThat(hoDuration.getSeasons()).isEqualTo(expectedSeasons);
        assertThat(hoDuration.getWeeks()).isEqualTo(expectedWeeks);
        assertThat(hoDuration.getDays()).isEqualTo(expectedDays);
        assertThat(hoDuration.getHours()).isEqualTo(expectedHours);
        assertThat(hoDuration.getMinutes()).isEqualTo(expectedMinutes);
        assertThat(hoDuration.getSeconds()).isEqualTo(expectedSeconds);
    }

    private static Stream<Arguments> getDaysInSeason() {
        return Stream.of(
            of(HODuration.fromSeconds(0), 0),
            of(new HODuration(0, 1), 1),
            of(new HODuration(0, 7), 7),
            of(new HODuration(0, 111), 111),
            of(new HODuration(0, 112), 0),
            of(new HODuration(0, 113), 1),

            of(new HODuration(1, 0), 0),
            of(new HODuration(1, 10), 10),
            of(new HODuration(2, 111), 111),

            of(new HODuration(0, -1), 111),
            of(new HODuration(0, -112), 0),
            of(new HODuration(0, -113), 111),
            of(new HODuration(-1, 10), 10)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getDaysInSeason(HODuration hoDuration, long expectedDaysInSeason) {
        assertThat(hoDuration.getDaysInSeason()).isEqualTo(expectedDaysInSeason);
    }

    private static Stream<Arguments> getTotalWeeks() {
        return Stream.of(
            of(HODuration.fromSeconds(0), 0),
            of(HODuration.of(0, 0, 6, 23, 59, 59), 0),
            of(HODuration.of(0, 1, 0, 0, 0, 0), 1),
            of(HODuration.of(0, 15, 0, 0, 0, 0), 15),
            of(HODuration.of(1, 0, 0, 0, 0, 0), 16),
            of(HODuration.of(1, 1, 0, 0, 0, 0), 17),
            of(HODuration.of(2, 3, 4, 0, 0, 0), 35),

            of(HODuration.of(0, 0, -1, 0, 0, 0), -1),
            of(HODuration.of(0, -1, 0, 0, 0, 0), -1),
            of(HODuration.of(-1, 0, 0, 0, 0, 0), -16),
            of(HODuration.of(-1, -1, 0, 0, 0, 0), -17)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getTotalWeeks(HODuration hoDuration, long expectedTotalWeeks) {
        assertThat(hoDuration.getTotalWeeks()).isEqualTo(expectedTotalWeeks);
    }

    @ParameterizedTest
    @MethodSource
    void between(String from, String to, HODuration expected) {
        final var fromDateTime = new HODateTime(Instant.parse(from));
        final var toDateTime = new HODateTime(Instant.parse(to));

        assertThat(HODuration.between(fromDateTime, toDateTime)).isEqualTo(expected);
    }

    private static Stream<Arguments> plus() {
        return Stream.of(
            of(new HODuration(1, 2), new HODuration(3, 4), new HODuration(4, 6)),
            of(new HODuration(-1, -2), new HODuration(3, 4), new HODuration(2, 2)),
            of(new HODuration(1, 2), new HODuration(-3, -4), new HODuration(-2, -2)),
            of(new HODuration(-1, -2), new HODuration(-3, -4), new HODuration(-4, -6)),
            of(new HODuration(-1, 2), new HODuration(-3, 4), new HODuration(-4, 6)),
            of(new HODuration(1, -2), new HODuration(3, -4), new HODuration(4, -6)),
            of(new HODuration(0, 100), new HODuration(0, 13), new HODuration(1, 1)),
            of(new HODuration(0, 0), new HODuration(0, 0), new HODuration(0, 0))
        );
    }

    @ParameterizedTest
    @MethodSource
    void plus(HODuration lhs, HODuration rhs, HODuration expected) {
        assertThat(lhs.plus(rhs)).isEqualTo(expected);
    }

    private static Stream<Arguments> minus() {
        return Stream.of(
            of(new HODuration(1, 2), new HODuration(3, 4), new HODuration(-2, -2)),
            of(new HODuration(-1, -2), new HODuration(3, 4), new HODuration(-5, 106)),
            of(new HODuration(1, 2), new HODuration(-3, -4), new HODuration(4, 6)),
            of(new HODuration(-1, -2), new HODuration(-3, -4), new HODuration(2, 2)),
            of(new HODuration(-1, 2), new HODuration(-3, 4), new HODuration(1, 110)),
            of(new HODuration(1, -2), new HODuration(3, -4), new HODuration(-2, 2)),
            of(new HODuration(0, 0), new HODuration(0, 0), new HODuration(0, 0))
        );
    }

    @ParameterizedTest
    @MethodSource
    void minus(HODuration lhs, HODuration rhs, HODuration expected) {
        assertThat(lhs.minus(rhs)).isEqualTo(expected);
    }

    private static Stream<Arguments> testToAgeStringAndToString() {
        return Stream.of(
            of(new HODuration(1, 2), "1 (2)"),
            of(new HODuration(1, -1), "0 (111)"),
            of(new HODuration(0, -2), "-1 (110)"),
            of(new HODuration(0, 114), "1 (2)"),
            of(new HODuration(0, 0), "0 (0)")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testToAgeStringAndToString(HODuration hoDuration, String expected) {
        final var ageString = hoDuration.toAgeString();
        final var toString = hoDuration.toString();
        assertThat(ageString).isEqualTo(expected);
        assertThat(toString).isEqualTo(ageString);
    }

    private static Stream<Arguments> toDouble() {
        return Stream.of(
            of(new HODuration(0, 0), 0.0),
            of(new HODuration(1, 0), 1.0),
            of(new HODuration(0, 56), 0.5),
            of(new HODuration(1, 56), 1.5),
            of(new HODuration(0, 112), 1.0),
            of(new HODuration(0, 168), 1.5),
            of(new HODuration(-1, 0), -1.0),
            of(new HODuration(0, -56), -0.5),
            of(new HODuration(0, -112), -1.0),
            of(new HODuration(-1, 56), -0.5)
        );
    }

    @ParameterizedTest
    @MethodSource
    void toDouble(HODuration hoDuration, double expected) {
        assertThat(hoDuration.toDouble()).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("equalDurations")
    void shouldBeEqual(HODuration duration1, HODuration duration2) {
        assertThat(duration1).isEqualTo(duration2);
    }

    @ParameterizedTest
    @MethodSource("differentDurations")
    void shouldNotBeEqual(HODuration duration1, HODuration duration2) {
        assertThat(duration1).isNotEqualTo(duration2);
    }

    @Test
    void equals_null() {
        assertThat(new HODuration(1, 2)).isNotEqualTo(null);
    }

    @Test
    void equals_differentType() {
        assertThat(new HODuration(1, 2)).isNotEqualTo("1 (2)");
    }

    @ParameterizedTest
    @MethodSource("equalDurations")
    void equalDurationsShouldHaveSameHashCode(HODuration duration1, HODuration duration2) {
        assertThat(duration1).isEqualTo(duration2);
        assertThat(duration1.hashCode()).isEqualTo(duration2.hashCode());
    }

    private static Stream<Arguments> compareTo() {
        return Stream.of(
            of(new HODuration(1, 2), new HODuration(3, 4), -1),
            of(new HODuration(-1, -2), new HODuration(3, 4), -1),
            of(new HODuration(1, 2), new HODuration(-3, -4), 1),
            of(new HODuration(-1, -2), new HODuration(-3, -4), 1),
            of(new HODuration(-1, 2), new HODuration(-3, 4), 1),
            of(new HODuration(1, -2), new HODuration(3, -4), -1),
            of(new HODuration(0, 112), new HODuration(1, 0), 0),
            of(new HODuration(1, 0), new HODuration(0, 112), 0),
            of(new HODuration(0, 0), new HODuration(0, 0), 0)
        );
    }

    @ParameterizedTest
    @MethodSource
    void compareTo(HODuration lhs, HODuration rhs, int expected) {
        assertThat(lhs.compareTo(rhs)).isEqualTo(expected);
    }

    private static Stream<Arguments> toHumanString() {
        return Stream.of(
            of(HODuration.of(0, 0, 0, 2, 3, 4), "2 hours, 3 minutes, 4 seconds"),
            of(HODuration.of(0, 0, 1, 0, 3, 4), "1 day, 3 minutes, 4 seconds"),
            of(HODuration.of(0, 0, 1, 2, 0, 4), "1 day, 2 hours, 4 seconds"),
            of(HODuration.of(0, 0, 1, 2, 3, 0), "1 day, 2 hours, 3 minutes"),
            of(HODuration.of(0, 0, 1, 2, 3, 4), "1 day, 2 hours, 3 minutes, 4 seconds"),
            of(HODuration.of(0, 1, 2, 3, 4, 5), "1 week, 2 days, 3 hours, 4 minutes, 5 seconds"),
            of(HODuration.of(1, 2, 3, 4, 5, 6), "1 season, 2 weeks, 3 days, 4 hours, 5 minutes, 6 seconds"),
            of(HODuration.fromSeconds(-1), "-1 season, 15 weeks, 6 days, 23 hours, 59 minutes, 59 seconds"),
            of(new HODuration(), EMPTY)
        );
    }

    @ParameterizedTest
    @MethodSource
    void toHumanString(HODuration hoDuration, String expected) {
        // given
        TranslationFacility.setTranslator(Translator.load(Translator.LANGUAGE_DEFAULT));

        // when
        final var result = hoDuration.toHumanString();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
