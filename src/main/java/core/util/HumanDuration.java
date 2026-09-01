package core.util;

import core.model.TranslationFacility;
import lombok.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ObjLongConsumer;
import java.util.function.ToLongFunction;

@Builder(access = AccessLevel.PACKAGE) // visibility for testing
@EqualsAndHashCode
@Getter
public class HumanDuration {

    private static final String DURATION_SUB_FORMAT = "%s %s";

    private static final long MINUTES_PER_HOUR = 60L;
    private static final long HOURS_PER_DAY = 24L;
    private static final long DAYS_PER_WEEK = 7L;
    private static final long WEEKS_PER_SEASON = 16L;

    private static final long SECONDS_IN_MINUTE = 60L;
    private static final long SECONDS_IN_HOUR = SECONDS_IN_MINUTE * MINUTES_PER_HOUR;
    private static final long SECONDS_IN_DAY = SECONDS_IN_HOUR * HOURS_PER_DAY;
    private static final long SECONDS_IN_WEEK = SECONDS_IN_DAY * DAYS_PER_WEEK;
    private static final long SECONDS_IN_SEASON = SECONDS_IN_WEEK * WEEKS_PER_SEASON;

    @RequiredArgsConstructor
    private enum Unit {
        SEASONS(SECONDS_IN_SEASON, "Duration.seasons.singular", "Duration.seasons.plural", HumanDurationBuilder::seasons, HumanDuration::getSeasons),
        WEEKS(SECONDS_IN_WEEK, "Duration.weeks.singular", "Duration.weeks.plural", HumanDurationBuilder::weeks, HumanDuration::getWeeks),
        DAYS(SECONDS_IN_DAY, "Duration.days.singular", "Duration.days.plural", HumanDurationBuilder::days, HumanDuration::getDays),
        HOURS(SECONDS_IN_HOUR, "Duration.hours.singular", "Duration.hours.plural", HumanDurationBuilder::hours, HumanDuration::getHours),
        MINUTES(SECONDS_IN_MINUTE, "Duration.minutes.singular", "Duration.minutes.plural", HumanDurationBuilder::minutes, HumanDuration::getMinutes),
        SECONDS(1, "Duration.seconds.singular", "Duration.seconds.plural", HumanDurationBuilder::seconds, HumanDuration::getSeconds);

        @Getter
        private final long secondsPerUnit;
        private final String translationKeySingular;
        private final String translationKeyPlural;
        private final ObjLongConsumer<HumanDurationBuilder> builderSetter;
        private final ToLongFunction<HumanDuration> getter;

        public long getValue(HumanDuration humanDuration) {
            return getter.applyAsLong(humanDuration);
        }

        public void setValue(HumanDurationBuilder humanDurationBuilder, long value) {
            builderSetter.accept(humanDurationBuilder, value);
        }

        public String getUnitAsString(long value) {
            return translateUnitSingularOrPlural(value == 1L);
        }

        private String translateUnitSingularOrPlural(boolean singular) {
            return TranslationFacility.trSingularOrPlural(singular, translationKeySingular, translationKeyPlural);
        }
    }

    private final long seasons;
    private final long weeks;
    private final long days;
    private final long hours;
    private final long minutes;
    private final long seconds;

    public static HumanDuration of(Duration duration) {
        return fromSeconds(duration.toSeconds());
    }

    public static HumanDuration fromSeconds(long duration) {
        final var builder = HumanDuration.builder();

        long remainingSeconds = duration;
        for (Unit unit : Unit.values()) {
            final long value = remainingSeconds / unit.getSecondsPerUnit();
            unit.setValue(builder, value);
            remainingSeconds %= unit.getSecondsPerUnit();
        }

        return builder.build();
    }

    public String toHumanString() {
        final List<String> strings = new ArrayList<>();
        for (Unit unit : Unit.values()) {
            final long value = unit.getValue(this);
            if (value != 0) {
                final var unitAsString = unit.getUnitAsString(value);
                strings.add(String.format(DURATION_SUB_FORMAT, value, unitAsString));
            }
        }
        return String.join(", ", strings);
    }
}
