package core.constants.player;

import core.model.TranslationFacility;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public enum  PlayerSkill {

	KEEPER(0),
	DEFENDING(1),
	WINGER(2),
	PLAYMAKING(3),
	SCORING(4),
	PASSING(5),
	STAMINA(6),
	FORM(7),
	SETPIECES(8),
	EXPERIENCE(9),
	LEADERSHIP(10),
	LOYALTY(11);

	private final int id;

	PlayerSkill(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

    private static final Map<Integer, PlayerSkill> MAP_ID_TO_PLAYER_SKILL =
        Arrays.stream(values()).collect(Collectors.toMap(PlayerSkill::toInt, Function.identity()));

	public String getLanguageString() {
		var b = new StringBuilder("ls.player.");
		switch (this) {
			case KEEPER, DEFENDING, WINGER, PLAYMAKING, SCORING, PASSING, STAMINA, SETPIECES -> b.append("skill.");
		}
		b.append(super.toString().toLowerCase());
		return TranslationFacility.tr(b.toString());
	}

    public static PlayerSkill fromInteger(Integer i) {
        return MAP_ID_TO_PLAYER_SKILL.getOrDefault(i, null);
    }

	public String getXMLElementName(){
		return switch (this){
			case KEEPER -> "Keeper";
			case DEFENDING -> "Defender";
			case PLAYMAKING -> "Playmaker";
			case WINGER -> "Winger";
			case PASSING -> "Passing";
			case SCORING -> "Scorer";
			case SETPIECES -> "SetPieces";
			default -> "unknown";
		};
	}
}
