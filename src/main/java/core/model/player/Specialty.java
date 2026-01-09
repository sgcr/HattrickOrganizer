package core.model.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Specialty {
    /*
    SpecialtyID
    Value	Description
0	No specialty
1	Technical
2	Quick
3	Powerful
4	Unpredictable
5	Head specialist
6	resilient
8	support
*/

    NoSpecialty(0, null),
    Technical(1, "ls.player.speciality.technical"),
    Quick(2, "ls.player.speciality.quick"),
    Powerful(3, "ls.player.speciality.powerful"),
    Unpredictable(4, "ls.player.speciality.unpredictable"),
    Head(5, "ls.player.speciality.head"),
    Regainer(6, "ls.player.speciality.regainer"),
    Not_used(7, null),
    Support(8, "ls.player.speciality.support");

    private final int id;
    private final String translationKey;

    private static final Map<Integer, Specialty> MAP_VALUE_TO_SPECIALTY =
            Stream.of(values()).collect(Collectors.toMap(Specialty::getId, Function.identity()));

    public static Specialty fromValueNullSafe(Integer value) {
        return Optional.ofNullable(value).map(MAP_VALUE_TO_SPECIALTY::get).orElse(null);
    }

    public static Integer getValueNullSafe(Specialty specialty) {
        return Optional.ofNullable(specialty).map(Specialty::getId).orElse(null);
    }
}
