package ru.investflow.mql.parser.parsing.preprocessor;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorIfDefParsing.parseDefine;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorIfDefParsing.parseIfDef;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorIfDefParsing.parseUndef;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorPropertyParsing.parsePropertyBlock;
import static ru.investflow.mql.psi.MQL4TokenTypes.PREPROCESSOR_BLOCK;

public class PreprocessorParsing {

    public static boolean parsePreprocessorBlock(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PreprocessorBlock")) {
            return false;
        }

        PsiBuilder.Marker m = enter_section_(b);
        boolean r = parsePropertyBlock(b, l + 1);
        if (!r) {
            r = parseDefine(b, l + 1);
            if (!r) {
                r = parseUndef(b, l + 1);
                if (!r) {
                    r = parseIfDef(b, l + 1);
                }
            }
        }
        exit_section_(b, m, PREPROCESSOR_BLOCK, r);
        return r;
    }

    public static boolean assertNoLineBreaksInRange(PsiBuilder b, int startOffset, @NotNull String errorMessage) {
        return assertNoLineBreaksInRange(b, startOffset, b.getCurrentOffset(), errorMessage);
    }

    public static boolean assertNoLineBreaksInRange(PsiBuilder b, int startOffset, int endOffset, @NotNull String errorMessage) {
        String text = b.getOriginalText().subSequence(startOffset, endOffset).toString();
        boolean hasEol = ParsingUtils.containsEndOfLine(text);
        if (hasEol) {
            b.error(errorMessage);
            return false;
        }
        return true;
    }
}
