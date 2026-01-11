package core.model.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class MatchTypeExtendedTest {

    private static Stream<Arguments> getById() {
        return Stream.of(
            of(-1, null),
            of(0, null),
            of(1000, null),
            of(1001, MatchTypeExtended.EMERALDCUP),
            of(1002, MatchTypeExtended.RUBYCUP),
            of(1003, MatchTypeExtended.SAPPHIRECUP),
            of(1004, MatchTypeExtended.CONSOLANTECUP),
            of(1005, null),
            of(1100, null),
            of(1101, MatchTypeExtended.DIVISIONBATTLE),
            of(9989, null),
            of(9990, MatchTypeExtended.GROUP_OFFICIAL),
            of(9991, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getById(int id, MatchTypeExtended expected) {
        assertThat(MatchTypeExtended.getById(id)).isEqualTo(expected);
    }
}
