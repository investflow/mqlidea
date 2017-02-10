package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorIncludeParsing.parseInclude;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorPropertyParsing.parseProperty;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceUntilNewLine;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceWithError;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.containsEndOfLineOrFile;

public class PreprocessorParsing implements MQL4Elements {

    public static boolean parsePreprocessorBlock(PsiBuilder b) {
        //parseDefine(b, l + 1)
        //parseUndef(b, l + 1)
        //parseIfDef(b, l + 1)
        //parseImport(b, l + 1)

        return parseInclude(b) ||
                parseProperty(b);
    }


    public static boolean assertNoLineBreaksInRange(PsiBuilder b, int startOffset, @NotNull String errorMessage) {
        return assertNoLineBreaksInRange(b, startOffset, b.getCurrentOffset(), errorMessage);
    }

    public static boolean assertNoLineBreaksInRange(PsiBuilder b, int startOffset, int endOffset, @NotNull String errorMessage) {
        boolean hasEol = hasLineBreaks(b, startOffset, endOffset);
        if (hasEol) {
            b.error(errorMessage);
            return false;
        }
        return true;
    }

    public static boolean hasLineBreaks(PsiBuilder b, int startOffset, int endOffset) {
        String text = b.getOriginalText().subSequence(startOffset, endOffset).toString();
        return ParsingUtils.containsEndOfLine(text);
    }

    public static void advanceWithErrorUntilNewLine(PsiBuilder b, int offset, String message) {
        advanceWithError(b, message);
        if (!containsEndOfLineOrFile(b, offset)) { // line ends after identifier -> end of block
            advanceUntilNewLine(b);
        }
    }
}
