package core.model.cup;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CupLevelIndexTest {

    private static Stream<Arguments> fromInteger() {
        return Stream.of(
            of(-1, null),
            of(0, CupLevelIndex.NONE),
            of(1, CupLevelIndex.EMERALD),
            of(2, CupLevelIndex.RUBY),
            of(3, CupLevelIndex.SAPPHIRE),
            of(4, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void fromInteger(int id, CupLevelIndex expected) {
        assertThat(CupLevelIndex.fromInt(id)).isEqualTo(expected);
    }
}
