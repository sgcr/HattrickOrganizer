package core.model.player;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class PlayerCategoryTest {

    private static Stream<Arguments> valueOf() {
        return Stream.of(
            of(-1, null),
            of(0, PlayerCategory.NoCategorySet),
            of(1, PlayerCategory.Keeper),
            of(2, PlayerCategory.WingBack),
            of(3, PlayerCategory.CentralDefender),
            of(4, PlayerCategory.Winger),
            of(5, PlayerCategory.InnerMidfield),
            of(6, PlayerCategory.Forward),
            of(7, PlayerCategory.Substitute),
            of(8, PlayerCategory.Reserve),
            of(9, PlayerCategory.Extra1),
            of(10, PlayerCategory.Extra2),
            of(11, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void valueOf(int id, PlayerCategory expected) {
        assertThat(PlayerCategory.valueOf(id)).isEqualTo(expected);
    }


    private static Stream<Arguments> idOf() {
        return Stream.concat(Stream.of(PlayerCategory.values()), Stream.of((PlayerCategory) null)).map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource
    void idOf(PlayerCategory playerCategory) {
        final var expected = Optional.ofNullable(playerCategory)
            .map(PlayerCategory::getId)
            .orElse(PlayerCategory.NoCategorySet.getId());
        assertThat(PlayerCategory.idOf(playerCategory)).isEqualTo(expected);
    }
}
