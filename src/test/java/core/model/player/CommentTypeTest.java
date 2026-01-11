package core.model.player;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class CommentTypeTest {

    private static Stream<Arguments> valueOf() {
        return Stream.of(
            of(-1, null),
            of(0, CommentType.HELLO),
            of(1, CommentType.FOUND_PLAYER),
            of(2, CommentType.NOT_USED),
            of(3, CommentType.TALENTED_IN_ONE_SKILL),
            of(4, CommentType.CURRENT_SKILL_LEVEL),
            of(5, CommentType.POTENTIAL_SKILL_LEVEL),
            of(6, CommentType.AVERAGE_SKILL_LEVEL),
            of(7, CommentType.SIGN_PLAYER_TO_TEAM),
            of(8, CommentType.NOT_USED2),
            of(9, CommentType.PLAYER_HAS_SPECIALTY),
            of(10, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void valueOf(int id, CommentType expected) {
        assertThat(CommentType.valueOf(id)).isEqualTo(expected);
    }
}

