package core.model.match;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class TournamentTypeTest {

    private static Stream<Arguments> getById() {
        return Stream.of(
            of(-1, null),
            of(0, TournamentType.NONE),
            of(1, null),
            of(2, null),
            of(3, TournamentType.LEAGUE_WITH_PLAYOFFS),
            of(4, TournamentType.CUP),
            of(5, null),
            of(6, null),
            of(7, null),
            of(8, null),
            of(9, null),
            of(10, TournamentType.DIVISIONBATTLE),
            of(11, null),
            of(4894807, TournamentType.U21_Friendlies),
            of(4878492, TournamentType.U21_Africa_Cup),
            of(4878490, TournamentType.U21_America_Cup),
            of(4878493, TournamentType.U21_Asia_Oceania_Cup),
            of(4878483, TournamentType.U21_Europe_Cup),
            of(4892615, TournamentType.U21_Nations_Cup),
            of(4891573, TournamentType.U21_Wildcard_Rounds),
            of(4892549, TournamentType.U21_World_Cup),
            of(5001311, TournamentType.Wildcard_Rounds),
            of(5001315, TournamentType.World_Cup),
            of(5001278, TournamentType.Africa_Cup),
            of(5001277, TournamentType.America_Cup),
            of(5001279, TournamentType.Asia_Oceania_Cup),
            of(5001273, TournamentType.Europe_Cup),
            of(5001319, TournamentType.Nations_Cup),
            of(5001325, TournamentType.NT_Friendlies)
        );
    }

    @ParameterizedTest
    @MethodSource
    void getById(int id, TournamentType expected) {
        assertThat(TournamentType.getById(id)).isEqualTo(expected);
    }
}
