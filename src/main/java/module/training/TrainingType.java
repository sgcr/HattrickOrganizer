package module.training;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrainingType {
    PAST_TRAINING(1),
    FUTURE_TRAINING(2);

    private final int value;
}
