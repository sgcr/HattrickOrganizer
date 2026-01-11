package core.constants.player;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class PlayerSkillTest {

    private static Stream<Arguments> fromInteger() {
        return Stream.of(
            of(-1, null),
            of(0, PlayerSkill.KEEPER),
            of(1, PlayerSkill.DEFENDING),
            of(2, PlayerSkill.WINGER),
            of(3, PlayerSkill.PLAYMAKING),
            of(4, PlayerSkill.SCORING),
            of(5, PlayerSkill.PASSING),
            of(6, PlayerSkill.STAMINA),
            of(7, PlayerSkill.FORM),
            of(8, PlayerSkill.SETPIECES),
            of(9, PlayerSkill.EXPERIENCE),
            of(10, PlayerSkill.LEADERSHIP),
            of(11, PlayerSkill.LOYALTY),
            of(12, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void fromInteger(int id, PlayerSkill expected) {
        assertThat(PlayerSkill.fromInteger(id)).isEqualTo(expected);
    }
}
