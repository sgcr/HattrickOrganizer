package module.training;

import module.training.Skills.ScoutCommentSkillTypeID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class SkillsTest {

    private static Stream<Arguments> ScoutCommentSkillTypeID_valueOf() {
        return Stream.of(
            of(-1, null),
            of(0, ScoutCommentSkillTypeID.AVERAGE),
            of(1, ScoutCommentSkillTypeID.KEEPER),
            of(2, ScoutCommentSkillTypeID.NOT_USED),
            of(3, ScoutCommentSkillTypeID.DEFENDING),
            of(4, ScoutCommentSkillTypeID.PLAYMAKER),
            of(5, ScoutCommentSkillTypeID.WINGER),
            of(6, ScoutCommentSkillTypeID.SCORER),
            of(7, ScoutCommentSkillTypeID.SET_PIECES),
            of(8, ScoutCommentSkillTypeID.PASSING),
            of(9, null),
            of(10, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void ScoutCommentSkillTypeID_valueOf(int id, ScoutCommentSkillTypeID expected) {
        assertThat(ScoutCommentSkillTypeID.valueOf(id)).isEqualTo(expected);
    }
}
