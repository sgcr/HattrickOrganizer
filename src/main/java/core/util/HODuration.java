package core.util;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class HODuration implements Comparable<HODuration> {

    private static final int DAYS_PER_SEASON = 7 * 16;

    private final long seasons;
    private final long days;

    public HODuration(long seasons, long days) {
        this.seasons = seasons + Math.floorDiv(days, DAYS_PER_SEASON);
        this.days = Math.floorMod(days, DAYS_PER_SEASON);
    }

    public long getSeasons() {
        return seasons;
    }

    public long getDays() {
        return days;
    }

    public static HODuration between(HODateTime from, HODateTime to) {
        return new HODuration(0, Duration.between(from.instant, to.instant).plus(12, ChronoUnit.HOURS).toDays());
    }

    public HODuration plus(HODuration diff) {
        return new HODuration(seasons + diff.seasons, days + diff.days);
    }

    public HODuration minus(HODuration diff) {
        return new HODuration(seasons - diff.seasons, days - diff.days);
    }

    public String toString() {
        return seasons + " (" + days + ")";
    }

    public double toDouble() {
        return seasons + days / (double) DAYS_PER_SEASON;
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
        return seasons == that.seasons && days == that.days;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasons, days);
    }

    @Override
    public int compareTo(@NotNull HODuration o) {
        int result = Long.compare(this.seasons, o.seasons);
        if (result == 0) {
            result = Long.compare(this.days, o.days);
        }
        return result;
    }
}
