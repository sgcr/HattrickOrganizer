package core.gui;

import core.model.Translator;
import core.util.HOLogger;

public class CompareTranslations {

    public void compare() {
        final Translator defaultTranslator = Translator.loadDefault();
        final Translator otherTranslator = Translator.load("German");

        final var setDiff =
            SetDiff.diff(defaultTranslator.getResourceBundle().keySet(), otherTranslator.getResourceBundle().keySet());

        setDiff.common().forEach(key -> {
            HOLogger.instance().debug(CompareTranslations.class, "Language '%s' vs '%s': Key '%s' is resident in both.".formatted(defaultTranslator.getLanguage(), otherTranslator.getLanguage(), key));
        });

        setDiff.onlyLeft().forEach(key -> {
            HOLogger.instance().debug(CompareTranslations.class, "Language '%s': Key '%s' is missing.".formatted(otherTranslator.getLanguage(), key));
        });

        setDiff.onlyRight().forEach(key -> {
            HOLogger.instance().debug(CompareTranslations.class, "Language '%s': Key '%s' seems superfluous!".formatted(otherTranslator.getLanguage(), key));
        });

        HOLogger.instance().debug(CompareTranslations.class, "Resident in both: %d".formatted(setDiff.common().size()));
        HOLogger.instance().debug(CompareTranslations.class, "Missing for '%s': %d".formatted(otherTranslator.getLanguage(), setDiff.onlyLeft().size()));
        HOLogger.instance().debug(CompareTranslations.class, "Superfluous for '%s': %d".formatted(otherTranslator.getLanguage(), setDiff.onlyRight().size()));
    }
}
