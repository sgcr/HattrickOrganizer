package core.util;

import core.model.TranslationFacility;
import core.model.Translator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

class HumanDurationTest {

    private static final long MINUTES_PER_HOUR = 60L;
    private static final long HOURS_PER_DAY = 24L;
    private static final long DAYS_PER_WEEK = 7L;
    private static final long WEEKS_PER_SEASON = 16L;

    private static final long SECONDS_IN_MINUTE = 60L;
    private static final long SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_PER_HOUR;
    private static final long SECONDS_IN_DAY = SECONDS_IN_HOUR * HOURS_PER_DAY;
    private static final long SECONDS_IN_WEEK = SECONDS_IN_DAY * DAYS_PER_WEEK;
    private static final long SECONDS_IN_SEASON = SECONDS_IN_WEEK * WEEKS_PER_SEASON;

    private static Stream<Arguments> secondsAndHumanDurations() {
        return Stream.of(
            Arguments.of(SECONDS_IN_HOUR * 2L + SECONDS_IN_MINUTE * 3L + 4L, HumanDuration.builder().hours(2).minutes(3).seconds(4).build()),
            Arguments.of(SECONDS_IN_DAY + SECONDS_IN_MINUTE * 3L + 4L, HumanDuration.builder().days(1).minutes(3).seconds(4).build()),
            Arguments.of(SECONDS_IN_DAY + SECONDS_IN_HOUR * 2L + 4L, HumanDuration.builder().days(1).hours(2).seconds(4).build()),
            Arguments.of(SECONDS_IN_DAY + SECONDS_IN_HOUR * 2L + SECONDS_IN_MINUTE * 3L, HumanDuration.builder().days(1).hours(2).minutes(3).build()),
            Arguments.of(SECONDS_IN_DAY + SECONDS_IN_HOUR * 2L + SECONDS_IN_MINUTE * 3L + 4L, HumanDuration.builder().days(1).hours(2).minutes(3).seconds(4).build()),
            Arguments.of(SECONDS_IN_SEASON, HumanDuration.builder().seasons(1).build()),
            Arguments.of(SECONDS_IN_SEASON + SECONDS_IN_WEEK * 2L, HumanDuration.builder().seasons(1).weeks(2).build()),
            Arguments.of(SECONDS_IN_SEASON + SECONDS_IN_WEEK * 2L + SECONDS_IN_DAY * 3L, HumanDuration.builder().seasons(1).weeks(2).days(3).build()),
            Arguments.of(SECONDS_IN_SEASON + SECONDS_IN_WEEK * 2L + SECONDS_IN_DAY * 3L + SECONDS_IN_HOUR * 4L, HumanDuration.builder().seasons(1).weeks(2).days(3).hours(4).build()),
            Arguments.of(SECONDS_IN_SEASON + SECONDS_IN_WEEK * 2L + SECONDS_IN_DAY * 3L + SECONDS_IN_HOUR * 4L + SECONDS_IN_MINUTE * 5L, HumanDuration.builder().seasons(1).weeks(2).days(3).hours(4).minutes(5L).build()),
            Arguments.of(SECONDS_IN_SEASON + SECONDS_IN_WEEK * 2L + SECONDS_IN_DAY * 3L + SECONDS_IN_HOUR * 4L + SECONDS_IN_MINUTE * 5L + 6L, HumanDuration.builder().seasons(1).weeks(2).days(3).hours(4).minutes(5L).seconds(6L).build()),
            Arguments.of(0L, HumanDuration.builder().build())
        );
    }

    private static Stream<Arguments> humanDurationToString() {
        return Stream.of(
            Arguments.of(HumanDuration.builder().hours(2).minutes(3).seconds(4).build(), "2 hours, 3 minutes, 4 seconds"),
            Arguments.of(HumanDuration.builder().days(1).minutes(3).seconds(4).build(), "1 day, 3 minutes, 4 seconds"),
            Arguments.of(HumanDuration.builder().days(1).hours(2).seconds(4).build(), "1 day, 2 hours, 4 seconds"),
            Arguments.of(HumanDuration.builder().days(1).hours(2).minutes(3).build(), "1 day, 2 hours, 3 minutes"),
            Arguments.of(HumanDuration.builder().days(1).hours(2).minutes(3).seconds(4).build(), "1 day, 2 hours, 3 minutes, 4 seconds"),
            Arguments.of(HumanDuration.builder().weeks(1).days(2).hours(3).minutes(4).seconds(5).build(), "1 week, 2 days, 3 hours, 4 minutes, 5 seconds"),
            Arguments.of(HumanDuration.builder().seasons(1).weeks(2).days(3).hours(4).minutes(5).seconds(6).build(), "1 season, 2 weeks, 3 days, 4 hours, 5 minutes, 6 seconds"),
            Arguments.of(HumanDuration.builder().build(), EMPTY)
        );
    }

    @ParameterizedTest
    @MethodSource("secondsAndHumanDurations")
    void of(long durationInSeconds, HumanDuration expected) {
        // given
        final Duration duration = Duration.ofSeconds(durationInSeconds);

        // when
        final HumanDuration result = HumanDuration.of(duration);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("secondsAndHumanDurations")
    void fromSeconds(long durationInSeconds, HumanDuration expected) {
        // when
        final HumanDuration result = HumanDuration.fromSeconds(durationInSeconds);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("humanDurationToString")
    void toHumanString(HumanDuration humanDuration, String expected) {
        // given
        TranslationFacility.setTranslator(Translator.load(Translator.LANGUAGE_DEFAULT));

        // when
        final var result = humanDuration.toHumanString();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
