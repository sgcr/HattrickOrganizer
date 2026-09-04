package core.util;

import core.model.TranslationFacility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.ToLongFunction;

public class HODuration implements Comparable<HODuration> {

    private static final long DAYS_PER_WEEK = 7;
    private static final long WEEKS_PER_SEASON = 16;

    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long MINUTES_PER_HOUR = 60L;
    private static final long SECONDS_PER_HOUR = MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
    private static final long HOURS_PER_DAY = 24L;
    private static final long SECONDS_PER_DAY = HOURS_PER_DAY * SECONDS_PER_HOUR;
    private static final long SECONDS_PER_WEEK = DAYS_PER_WEEK * SECONDS_PER_DAY;
    private static final long SECONDS_PER_SEASON = WEEKS_PER_SEASON * SECONDS_PER_WEEK;

    private static final String DURATION_SUB_FORMAT = "%s %s";

    private final long totalSeconds;

    public HODuration() {
        this.totalSeconds = 0;
    }

    public HODuration(long seasons, long days) {
        this.totalSeconds = seasons * SECONDS_PER_SEASON + SECONDS_PER_DAY * days;
    }

    private HODuration(long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public static HODuration of(Duration duration) {
        return fromSeconds(duration.toSeconds());
    }

    public static HODuration of(
        long seasons,
        long weeks,
        long days,
        long hours,
        long minutes,
        long seconds
    ) {
        return fromSeconds(
            seasons * SECONDS_PER_SEASON
                + weeks * SECONDS_PER_WEEK
                + days * SECONDS_PER_DAY
                + hours * SECONDS_PER_HOUR
                + minutes * SECONDS_PER_MINUTE
                + seconds
        );
    }

    public static HODuration fromSeconds(long totalSeconds) {
        return new HODuration(totalSeconds);
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public long getSeasons() {
        return Math.floorDiv(totalSeconds, SECONDS_PER_SEASON);
    }

    public long getWeeks() {
        return Math.floorMod(totalSeconds, SECONDS_PER_SEASON) / SECONDS_PER_WEEK;
    }

    public long getDays() {
        return Math.floorMod(totalSeconds, SECONDS_PER_WEEK) / SECONDS_PER_DAY;
    }

    public long getHours() {
        return Math.floorMod(totalSeconds, SECONDS_PER_DAY) / SECONDS_PER_HOUR;
    }

    public long getMinutes() {
        return Math.floorMod(totalSeconds, SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
    }

    public long getSeconds() {
        return Math.floorMod(totalSeconds, SECONDS_PER_MINUTE);
    }

    /**
     * Returns the remaining number of days within the current season, including days represented by full weeks.
     *
     * @return number of days within the season, from 0 to 111
     * @see #getDays()
     * @see #getWeeks()
     */
    public long getDaysInSeason() {
        return Math.floorMod(totalSeconds, SECONDS_PER_SEASON) / SECONDS_PER_DAY;
    }

    public long getTotalWeeks() {
        return getSeasons() * WEEKS_PER_SEASON + getWeeks();
    }

    public static HODuration between(HODateTime from, HODateTime to) {
        return new HODuration(0, Duration.between(from.instant, to.instant).plus(12, ChronoUnit.HOURS).toDays());
    }

    public HODuration plus(HODuration diff) {
        return fromSeconds(totalSeconds + diff.totalSeconds);
    }

    public HODuration minus(HODuration diff) {
        return fromSeconds(totalSeconds - diff.totalSeconds);
    }

    @Override
    public String toString() {
        return toAgeString();
    }

    public String toAgeString() {
        return getSeasons() + " (" + getDaysInSeason() + ")";
    }

    public double toDouble() {
        return totalSeconds / (double) SECONDS_PER_SEASON;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HODuration that = (HODuration) o;
        return totalSeconds == that.totalSeconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalSeconds);
    }

    @Override
    public int compareTo(@NotNull HODuration o) {
        return Long.compare(this.totalSeconds, o.totalSeconds);
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

    @RequiredArgsConstructor
    private enum Unit {
        SEASONS(SECONDS_PER_SEASON, "Duration.seasons.singular", "Duration.seasons.plural", HODuration::getSeasons),
        WEEKS(SECONDS_PER_WEEK, "Duration.weeks.singular", "Duration.weeks.plural", HODuration::getWeeks),
        DAYS(SECONDS_PER_DAY, "Duration.days.singular", "Duration.days.plural", HODuration::getDays),
        HOURS(SECONDS_PER_HOUR, "Duration.hours.singular", "Duration.hours.plural", HODuration::getHours),
        MINUTES(SECONDS_PER_MINUTE, "Duration.minutes.singular", "Duration.minutes.plural", HODuration::getMinutes),
        SECONDS(1, "Duration.seconds.singular", "Duration.seconds.plural", HODuration::getSeconds);

        @Getter
        private final long secondsPerUnit;
        private final String translationKeySingular;
        private final String translationKeyPlural;
        private final ToLongFunction<HODuration> getter;

        public long getValue(HODuration hoDuration) {
            return getter.applyAsLong(hoDuration);
        }

        public String getUnitAsString(long value) {
            return translateUnitSingularOrPlural(value == 1L);
        }

        private String translateUnitSingularOrPlural(boolean singular) {
            return TranslationFacility.trSingularOrPlural(singular, translationKeySingular, translationKeyPlural);
        }
    }
}
