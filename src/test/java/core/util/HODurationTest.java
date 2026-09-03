package core.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class HODurationTest {

    private static final int DAYS_PER_SEASON = 7 * 16;

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
    void ctor_seasonsAndDays(int seasons, int days, int expectedSeasons, int expectedDays) {
        final var hoDuration = new HODuration(seasons, days);
        assertThat(hoDuration.getSeasons()).isEqualTo(expectedSeasons);
        assertThat(hoDuration.getDays()).isEqualTo(expectedDays);
    }


}
