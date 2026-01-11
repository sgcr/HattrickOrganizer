package module.youth;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class YouthTrainingTypeTest {

    private static Stream<Arguments> valueOf() {
        return Stream.of(
            of(0, null),
            of(1, YouthTrainingType.IndividualTraining),
            of(2, YouthTrainingType.SetPieces),
            of(3, YouthTrainingType.Defending),
            of(4, YouthTrainingType.Scoring),
            of(5, YouthTrainingType.Winger),
            of(6, YouthTrainingType.Shooting),
            of(7, YouthTrainingType.Passing),
            of(8, YouthTrainingType.Playmaking),
            of(9, YouthTrainingType.Goalkeeping),
            of(10, YouthTrainingType.ThroughPassing),
            of(11, YouthTrainingType.DefencePositions),
            of(12, YouthTrainingType.WingAttacking),
            of(13, null)
        );
    }

    @ParameterizedTest
    @MethodSource
    void valueOf(int id, YouthTrainingType expected) {
        assertThat(YouthTrainingType.valueOf(id)).isEqualTo(expected);
    }
}
