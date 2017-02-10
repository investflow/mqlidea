package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.advanceWithErrorUntilNewLine;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.assertNoLineBreaksInRange;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.containsEndOfLineOrFile;

public class PreprocessorIncludeParsing implements MQL4Elements {

    public static boolean parseInclude(PsiBuilder b) {
        if (b.getTokenType() != INCLUDE_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        int startOffset = b.getCurrentOffset();
        b.advanceLexer(); // '#include' keyword
        try {
            String errorMessage = "#include argument is expected";
            // line breaks are not allowed for #include
            if (!assertNoLineBreaksInRange(b, startOffset, errorMessage)) {
                return true;
            }
            IElementType tt = b.getTokenType();
            if (tt != STRING_LITERAL && tt != INCLUDE_STRING_LITERAL) {
                advanceWithErrorUntilNewLine(b, b.getCurrentOffset(), errorMessage);
                return true;
            }
            int paramOffset = b.getCurrentOffset();
            b.advanceLexer(); // include parameter
            if (!containsEndOfLineOrFile(b, paramOffset)) {
                advanceWithErrorUntilNewLine(b, paramOffset, ParsingErrors.UNEXPECTED_TOKEN);
            }
        } finally {
            m.done(PREPROCESSOR_INCLUDE_BLOCK);
        }

        return true;
    }

}
