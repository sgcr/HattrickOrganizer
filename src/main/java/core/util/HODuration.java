package core.util;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class HODuration implements Comparable<HODuration> {

    private static final long DAYS_PER_WEEK = 7;
    private static final long WEEKS_PER_SEASON = 16;
    private static final long DAYS_PER_SEASON = DAYS_PER_WEEK * WEEKS_PER_SEASON;

    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long MINUTES_PER_HOUR = 60L;
    private static final long SECONDS_PER_HOUR = MINUTES_PER_HOUR * SECONDS_PER_MINUTE;
    private static final long HOURS_PER_DAY = 24L;
    private static final long SECONDS_PER_DAY = HOURS_PER_DAY * SECONDS_PER_HOUR;
    private static final long SECONDS_PER_WEEK = DAYS_PER_WEEK * SECONDS_PER_DAY;
    private static final long SECONDS_PER_SEASON = WEEKS_PER_SEASON * SECONDS_PER_WEEK;

    private final long totalSeconds;

    public HODuration(long seasons, long days) {
        this.totalSeconds = seasons * SECONDS_PER_SEASON + SECONDS_PER_DAY * days;
    }

    private HODuration(long totalSeconds) {
        this.totalSeconds = totalSeconds;
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

    public long getDays() {
        return Math.floorMod(totalSeconds, SECONDS_PER_SEASON) / SECONDS_PER_DAY;
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
        return getSeasons() + " (" + getDays() + ")";
    }

    public double toDouble() {
        return totalSeconds / (double) SECONDS_PER_SEASON;
//        return getSeasons() + getDays() / (double) DAYS_PER_SEASON;
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
}
