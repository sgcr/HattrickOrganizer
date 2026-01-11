package core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum StaffType {

	NONE(0),
	ASSISTANTTRAINER(1),
	MEDIC(2),
	SPOKESPERSON(3),
	SPORTPSYCHOLOGIST(4),
	FORMCOACH(5),
	FINANCIALDIRECTOR(6),
	TACTICALASSISTANT(7);

	private final int id;

    private static final Map<Integer, StaffType> MAP_ID_TO_STAFF_TYPE =
        Arrays.stream(values()).collect(Collectors.toMap(StaffType::getId, Function.identity()));

    public static StaffType getById(int id) {
        return MAP_ID_TO_STAFF_TYPE.getOrDefault(id, null);
    }

		public String getName() {
			 switch (this) {
		         case ASSISTANTTRAINER:
		             return TranslationFacility.tr("ls.club.staff.assistantcoach");

		         case MEDIC:
		             return TranslationFacility.tr("ls.club.staff.medic");

		         case SPOKESPERSON:
		             return TranslationFacility.tr("ls.club.staff.spokesperson");

		         case SPORTPSYCHOLOGIST:
		             return TranslationFacility.tr("ls.club.staff.sportspsychologist");

		         case FORMCOACH:
		             return TranslationFacility.tr("ls.club.staff.formcoach");

		         case FINANCIALDIRECTOR:
		             return TranslationFacility.tr("ls.club.staff.financialdirector");

		         case TACTICALASSISTANT:
		           return TranslationFacility.tr("ls.club.staff.tacticalassistant");

		           //Error?
		         default:
		             return "unknown";

			 }
		}
}
