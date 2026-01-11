package core.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class StaffTypeTest {

    private static Stream<Arguments> getById() {
        return Stream.of(
            of(-1, null),
            of(0, StaffType.NONE),
            of(1, StaffType.ASSISTANTTRAINER),
            of(2, StaffType.MEDIC),
            of(3, StaffType.SPOKESPERSON),
            of(4, StaffType.SPORTPSYCHOLOGIST),
            of(5, StaffType.FORMCOACH),
            of(6, StaffType.FINANCIALDIRECTOR),
            of(7, StaffType.TACTICALASSISTANT),
            of(8, null),
            of(9, null),
            of(10, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getById(int id, StaffType expected) {
        assertThat(StaffType.getById(id)).isEqualTo(expected);
    }
}
