package core.model.cup;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CupLevelTest {

    private static Stream<Arguments> fromInteger() {
        return Stream.of(
            of(-1, null),
            of(0, CupLevel.NONE),
            of(1, CupLevel.NATIONALorDIVISIONAL),
            of(2, CupLevel.CHALLENGER),
            of(3, CupLevel.CONSOLATION),
            of(4, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void fromInteger(int id, CupLevel expected) {
        assertThat(CupLevel.fromInt(id)).isEqualTo(expected);
    }
}
