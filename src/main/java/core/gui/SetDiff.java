package core.gui;

import java.util.HashSet;
import java.util.Set;

public record SetDiff<T>(Set<T> onlyLeft,
                         Set<T> common,
                         Set<T> onlyRight) {
    public static <T> SetDiff<T> diff(Set<T> left, Set<T> right) {
        var onlyLeft = new HashSet<>(left);
        onlyLeft.removeAll(right);

        var common = new HashSet<>(left);
        common.retainAll(right);

        var onlyRight = new HashSet<>(right);
        onlyRight.removeAll(left);

        return new SetDiff<>(onlyLeft, common, onlyRight);
    }
}
