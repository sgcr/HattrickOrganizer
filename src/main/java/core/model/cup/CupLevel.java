package core.model.cup;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum CupLevel {

    NONE(0),
    NATIONALorDIVISIONAL(1),  // National/Divisional cup
    CHALLENGER(2), // Challenger cup
    CONSOLATION(3);  // Consolation cup

    private final int id;

    private static final Map<Integer, CupLevel> MAP_ID_TO_CUP_LEVEL =
        Arrays.stream(values()).collect(Collectors.toMap(CupLevel::getId, Function.identity()));

    public static CupLevel fromInt(Integer iCupLevel) {
        return MAP_ID_TO_CUP_LEVEL.getOrDefault(iCupLevel, null);
    }
}
