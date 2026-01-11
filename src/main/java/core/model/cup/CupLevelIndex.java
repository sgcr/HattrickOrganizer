package core.model.cup;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum CupLevelIndex {

    NONE(0), // i.e. MatchType is not 3.
    EMERALD(1),  // EMERALD, National or Divisional cup
    RUBY(2), // Ruby cup
    SAPPHIRE(3);  // Sapphire cup

    private final int id;

    private static final Map<Integer, CupLevelIndex> MAP_ID_TO_CUP_LEVEL_INDEX =
        Arrays.stream(values()).collect(Collectors.toMap(CupLevelIndex::getId, Function.identity()));

    public static CupLevelIndex fromInt(Integer iCupLevelIndex) {
        return MAP_ID_TO_CUP_LEVEL_INDEX.getOrDefault(iCupLevelIndex, null);
    }
}
