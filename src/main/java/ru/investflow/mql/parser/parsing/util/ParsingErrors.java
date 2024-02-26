package ru.investflow.mql.parser.parsing.util;

import com.intellij.lang.PsiBuilder;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.settings.MQL4PluginSettings;

/**
 * List of common parsing errors. To be refactored.
 */
public class ParsingErrors {

    public static final String UNEXPECTED_TOKEN = "Unexpected token";
    public static final String NO_MATCHING_CLOSING_BRACKET = "No matching closing bracket";
    public static final String IDENTIFIER_EXPECTED = "Identifier expected";


    /**
     * Advances current token and sets error message on it.
     */
    public static void advanceWithError(@NotNull PsiBuilder b, @NotNull String message) {
        if (isEAOn()) {
            PsiBuilder.Marker errorBlock = b.mark();
            b.advanceLexer();
            errorBlock.error(message);
        } else {
            b.advanceLexer();
        }
    }

    public static void error(@NotNull PsiBuilder b, @NotNull String message) {
        if (isEAOn()) {
            b.error(message);
        }
    }

    private static boolean isEAOn() {
        MQL4PluginSettings settings = MQL4PluginSettings.getInstance();
        return settings == null || settings.performErrorAnalysis();
    }
}
