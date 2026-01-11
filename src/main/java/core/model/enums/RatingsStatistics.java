package core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RatingsStatistics {
    POWER_RATINGS(1),
    HATSTATS_TOTAL(2),
    HATSTATS_DEF(3),
    HATSTATS_MID(4),
    HATSTATS_OFF(5);

    private final int value;
}
