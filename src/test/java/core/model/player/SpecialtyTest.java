package core.model.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpecialtyTest {

    @Test
    void constant_NoSpecialty() {
        assertThat(Specialty.NoSpecialty.getId()).isEqualTo(0);
    }

    @Test
    void constant_Technical() {
        assertThat(Specialty.Technical.getId()).isEqualTo(1);
    }

    @Test
    void constant_Quick() {
        assertThat(Specialty.Quick.getId()).isEqualTo(2);
    }

    @Test
    void constant_Powerful() {
        assertThat(Specialty.Powerful.getId()).isEqualTo(3);
    }

    @Test
    void constant_Unpredictable() {
        assertThat(Specialty.Unpredictable.getId()).isEqualTo(4);
    }

    @Test
    void constant_Head() {
        assertThat(Specialty.Head.getId()).isEqualTo(5);
    }

    @Test
    void constant_Regainer() {
        assertThat(Specialty.Regainer.getId()).isEqualTo(6);
    }

    @Test
    void constant_Not_used() {
        assertThat(Specialty.Not_used.getId()).isEqualTo(7);
    }

    @Test
    void constant_Support() {
        assertThat(Specialty.Support.getId()).isEqualTo(8);
    }

    private static Stream<Arguments> fromValueNullSafe() {
        return Stream.of(
                Arguments.of(-1, null),
                Arguments.of(0, Specialty.NoSpecialty),
                Arguments.of(1, Specialty.Technical),
                Arguments.of(2, Specialty.Quick),
                Arguments.of(3, Specialty.Powerful),
                Arguments.of(4, Specialty.Unpredictable),
                Arguments.of(5, Specialty.Head),
                Arguments.of(6, Specialty.Regainer),
                Arguments.of(7, Specialty.Not_used),
                Arguments.of(8, Specialty.Support),
                Arguments.of(9, null),
                Arguments.of(10, null),
                Arguments.of(31337, null),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void fromValueNullSafe(Integer value, Specialty expected) {
        assertThat(Specialty.fromValueNullSafe(value)).isEqualTo(expected);
    }

    private static Stream<Arguments> getValueNullSafe() {
        return Stream.of(
                Arguments.of(Specialty.NoSpecialty, 0),
                Arguments.of(Specialty.Technical, 1),
                Arguments.of(Specialty.Quick, 2),
                Arguments.of(Specialty.Powerful, 3),
                Arguments.of(Specialty.Unpredictable, 4),
                Arguments.of(Specialty.Head, 5),
                Arguments.of(Specialty.Regainer, 6),
                Arguments.of(Specialty.Not_used, 7),
                Arguments.of(Specialty.Support, 8),
                Arguments.of(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getValueNullSafe(Specialty specialty, Integer expected) {
        assertThat(Specialty.getValueNullSafe(specialty)).isEqualTo(expected);
    }
}