package core.constants.player;

import core.datatype.CBItem;
import core.model.TranslationFacility;
import core.model.match.Weather;
import core.model.player.Specialty;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PlayerSpeciality {

	public static final int NO_SPECIALITY = 0;
	public static final int TECHNICAL = 1;
	public static final int QUICK = 2;
	public static final int POWERFUL = 3;
	public static final int UNPREDICTABLE = 4;
	public static final int HEAD = 5;
	public static final int REGAINER = 6;
	public static final int SUPPORT = 8;

	private static final int[] VALUES = {
			NO_SPECIALITY,
			TECHNICAL,
			QUICK,
			POWERFUL,
			UNPREDICTABLE,
			HEAD,
			REGAINER,
			SUPPORT
	};

	public static int[] values() {
		return VALUES;
	}

	public static final int NoWeatherEffect = 0;
	public static final int PositiveWeatherEffect = 1;
	public static final int NegativeWeatherEffect = -1;

	public static final float ImpactWeatherEffect = 0.05f;

	private static final Map<Integer, Specialty> MAP_ID_TO_SPECIALTY = Map.ofEntries(
			Map.entry(NO_SPECIALITY, Specialty.NoSpecialty),
			Map.entry(TECHNICAL, Specialty.Technical),
			Map.entry(QUICK, Specialty.Quick),
			Map.entry(POWERFUL, Specialty.Powerful),
			Map.entry(UNPREDICTABLE, Specialty.Unpredictable),
			Map.entry(HEAD, Specialty.Head),
			Map.entry(REGAINER, Specialty.Regainer),
			Map.entry(SUPPORT, Specialty.Support)
	);

	private static final Map<Integer, String> MAP_ID_TO_TRANSLATION = Arrays.stream(values())
			.mapToObj(value -> Map.entry(value, getTranslatedSpecialityText(value)))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

	public static final CBItem[] ITEMS = Arrays.stream(values())
			.mapToObj(PlayerSpeciality::createCBItem)
			.toArray(CBItem[]::new);

	private static CBItem createCBItem(int id) {
		return new CBItem(getTranslatedSpecialityText(id), id);
	}

	private PlayerSpeciality() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static String toString(Integer speciality) {
		return Optional.ofNullable(speciality)
				.map(s -> MAP_ID_TO_TRANSLATION.getOrDefault(s, TranslationFacility.tr("Unbestimmt")))
				.orElse(TranslationFacility.tr("Unbestimmt"));
	}

	private static String getTranslatedSpecialityText(int id) {
		return TranslationFacility.tr(mapIdToSpeciality(id).getTranslationKey());
	}

	private static Specialty mapIdToSpeciality(int id) {
		return Optional.ofNullable(MAP_ID_TO_SPECIALTY.get(id))
				.orElseThrow(() -> new RuntimeException("ID %d is not defined to be mapped to a Speciality.".formatted(id)));
	}

	public static int getWeatherEffect(Weather weather, int playerSpecialty) {
		switch (weather) {
			case SUNNY:
				if (playerSpecialty == TECHNICAL) {
					return PositiveWeatherEffect; // Technical players gain 5% on all their skills in the sun
				} else if (playerSpecialty == POWERFUL) {
					return NegativeWeatherEffect;   // Powerful players loose 5% on all their skills in the rain
				} else if (playerSpecialty == QUICK) {
					return NegativeWeatherEffect;  // Quick players lose 5% in the rain and in the sun.
				}

				break;

			case RAINY:
				if (playerSpecialty == TECHNICAL) {
					return NegativeWeatherEffect;  // Technical players loose 5% on all their skills in the sun
				} else if (playerSpecialty == POWERFUL) {
					return PositiveWeatherEffect;  // Powerful players gain 5% on all their skills in the rain
				} else if (playerSpecialty == QUICK) {
					return NegativeWeatherEffect;  // Quick players lose 5% in the rain and in the sun.
				}
				break;

			default:
				break;
		}

		return NoWeatherEffect;
	}
}
