package core.model.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum CommentType {

    HELLO(0),
    FOUND_PLAYER(1),
    NOT_USED(2),
    TALENTED_IN_ONE_SKILL(3),
    CURRENT_SKILL_LEVEL(4),
    POTENTIAL_SKILL_LEVEL(5),
    AVERAGE_SKILL_LEVEL(6),
    SIGN_PLAYER_TO_TEAM(7),
    NOT_USED2(8),
    PLAYER_HAS_SPECIALTY(9);

    private final int value;

    private static final Map<Integer, CommentType> MAP_VALUE_TO_COMMENT_TYPE =
        Arrays.stream(values()).collect(Collectors.toMap(CommentType::getValue, Function.identity()));

    public static CommentType valueOf(Integer id) {
        return MAP_VALUE_TO_COMMENT_TYPE.get(id);
    }
}
