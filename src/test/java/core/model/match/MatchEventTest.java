package core.model.match;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchEventTest {

    private static final Set<MatchEvent.MatchEventID> OWN_GOAL_EVENTS = Set.of(
        MatchEvent.MatchEventID.SE_GOAL_UNPREDICTABLE_OWN_GOAL,
        MatchEvent.MatchEventID.SE_NO_GOAL_UNPREDICTABLE_OWN_GOAL_ALMOST
    );

    private static Stream<Arguments> ownGoalEvents() {
        return OWN_GOAL_EVENTS.stream()
            .map(MatchEvent.MatchEventID::getValue)
            .map(Arguments::of);
    }

    private static Stream<Arguments> nonOwnGoalEvents() {
        return Stream.of(MatchEvent.MatchEventID.values())
            .filter(matchEventId -> !OWN_GOAL_EVENTS.contains(matchEventId))
            .map(MatchEvent.MatchEventID::getValue)
            .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("ownGoalEvents")
    void test_static_isOwnGoalEvent_isTrue(int matchEventId) {
        assertThat(MatchEvent.isOwnGoalEvent(matchEventId)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonOwnGoalEvents")
    void test_static_isOwnGoalEvent_isFalse(int matchEventId) {
        assertThat(MatchEvent.isOwnGoalEvent(matchEventId)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("ownGoalEvents")
    void test_isOwnGoalEvent_isTrue(int matchEventId) {
        final MatchEvent matchEvent = new MatchEvent();
        matchEvent.setMatchEventID(matchEventId);
        assertThat(matchEvent.isOwnGoalEvent()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("nonOwnGoalEvents")
    void test_isOwnGoalEvent_isFalse(int matchEventId) {
        final MatchEvent matchEvent = new MatchEvent();
        matchEvent.setMatchEventID(matchEventId);
        assertThat(matchEvent.isOwnGoalEvent()).isFalse();
    }

    private static Stream<Arguments> validMatchEventIds() {
        return Stream.of(MatchEvent.MatchEventID.values())
            .filter(matchEventID -> !OWN_GOAL_EVENTS.contains(matchEventID))
            .map(MatchEvent.MatchEventID::getValue)
            .map(Arguments::of);
    }

    private static Stream<Arguments> invalidMatchEventIds() {
        final var validIdSet = Stream.of(MatchEvent.MatchEventID.values()).map(MatchEvent.MatchEventID::getValue)
            .collect(Collectors.toSet());
        return IntStream.rangeClosed(-10, 1000)
            .filter(i -> !validIdSet.contains(i))
            .mapToObj(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("validMatchEventIds")
    void findMatchEventId(int value) {
        final var matchEventID = MatchEvent.MatchEventID.findMatchEventId(value);
        assertThat(matchEventID).isPresent();
        assertThat(matchEventID.get().getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("invalidMatchEventIds")
    void findMatchEventId_invalid(int value) {
        final var matchEventID = MatchEvent.MatchEventID.findMatchEventId(value);
        assertThat(matchEventID).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("validMatchEventIds")
    void ofMatchEventId(int value) {
        final var matchEventID = MatchEvent.MatchEventID.ofMatchEventId(value);
        assertThat(matchEventID.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("invalidMatchEventIds")
    void ofMatchEventId_invalid_throws(int value) {
        assertThatThrownBy(() ->
            MatchEvent.MatchEventID.ofMatchEventId(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("No MatchEventID found for ID %d".formatted(value));
    }
}
