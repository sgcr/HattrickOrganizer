package core.model.enums;

import core.model.TranslationFacility;
import core.model.match.IMatchType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum MatchTypeExtended implements IMatchType {

	EMERALDCUP(1001), // That match type is not part of HT CHPP API. It is created within HO for convenience with existing DB structure
	RUBYCUP(1002), // That match type is not part of HT CHPP API. It is created within HO for convenience with existing DB structure
	SAPPHIRECUP(1003), // That match type is not part of HT CHPP API. It is created within HO for convenience with existing DB structure
	CONSOLANTECUP(1004), // That match type is not part of HT CHPP API. It is created within HO for convenience with existing DB structure
	DIVISIONBATTLE(1101), // That match type is not part of HT CHPP API. It is created within HO for convenience with existing DB structure
 	GROUP_OFFICIAL(9990); // Supposed to replace constants declared in SpielePanel

	private final int id;

    private static final Map<Integer, MatchTypeExtended> MAP_ID_TO_MATCH_TYPE_EXTENDED =
        Arrays.stream(values()).collect(Collectors.toMap(MatchTypeExtended::getId, Function.identity()));

    public static MatchTypeExtended getById(int id) {
        return MAP_ID_TO_MATCH_TYPE_EXTENDED.get(id);
    }

	@Override
	public int getMatchTypeId() {
		return MatchType.CUP.getId();
	}

	@Override
	public int getIconArrayIndex() {
		return switch (this) {
			case EMERALDCUP -> 4;
			case RUBYCUP -> 5;
			case SAPPHIRECUP -> 6;
			case CONSOLANTECUP -> 12;
			case DIVISIONBATTLE -> 13;
			default -> 11;
		};
	}

	@Override
	public String getName() {
		return switch (this) {
			case EMERALDCUP -> TranslationFacility.tr("ls.match.matchtype.emerald_cup");
			case RUBYCUP -> TranslationFacility.tr("ls.match.matchtype.ruby_cup");
			case SAPPHIRECUP -> TranslationFacility.tr("ls.match.matchtype.sapphire_cup");
			case CONSOLANTECUP -> TranslationFacility.tr("ls.match.matchtype.consolante_cup");
			case DIVISIONBATTLE -> TranslationFacility.tr("ls.match.matchtype.division_battle");
			default -> "unknown";
		};
	}
}
