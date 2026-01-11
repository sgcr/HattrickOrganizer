package core.model.player;

import core.model.TranslationFacility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum PlayerCategory {

    //    PlayerCategoryID
    //    Value	Description
    //1	Keeper
    //2	Wing back
    //3	Central defender
    //4	Winger
    //5	Inner midfielder
    //6	Forward
    //7	Substitute
    //8	Reserve
    //9	Extra 1
    //            10	Extra 2
    //            0	No category set

    NoCategorySet(0),
    Keeper(1),
    WingBack(2),
    CentralDefender(3),
    Winger(4),
    InnerMidfield(5),
    Forward(6),
    Substitute(7),
    Reserve(8),
    Extra1(9),
    Extra2(10);

    private final int id;

    private static final Map<Integer, PlayerCategory> MAP_ID_TO_PLAYER_CATEGORY =
        Arrays.stream(values()).collect(Collectors.toMap(PlayerCategory::getId, Function.identity()));

    public static String StringValueOf(PlayerCategory value) {
        if (value == null || value == NoCategorySet) {
            return "";
        }
        return TranslationFacility.tr("ls.player.category." + value._toString());
    }

    private String _toString() {
        return super.toString();
    }

    @Override
    public String toString() {
        return StringValueOf(this);
    }

    public static PlayerCategory valueOf(Integer id) {
        return Optional.ofNullable(id).map(MAP_ID_TO_PLAYER_CATEGORY::get).orElse(null);
    }

    public static int idOf(PlayerCategory category){
        return Optional.ofNullable(category).map(PlayerCategory::getId).orElse(NoCategorySet.getId());
    }
}
