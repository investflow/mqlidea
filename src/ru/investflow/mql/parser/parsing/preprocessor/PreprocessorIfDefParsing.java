package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.assertNoLineBreaksInRange;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.completePPStatement;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.IDENTIFIER_EXPECTED;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.error;

public class PreprocessorIfDefParsing implements MQL4Elements {

    public static boolean parseUndef(PsiBuilder b) {
        if (b.getTokenType() != UNDEF_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        int startOffset = b.getCurrentOffset();
        b.advanceLexer(); // '#undef' keyword
        try {
            if (!assertNoLineBreaksInRange(b, startOffset, ParsingErrors.IDENTIFIER_EXPECTED)) {
                return true;
            }
            IElementType tt = b.getTokenType();
            if (tt != IDENTIFIER) {
                error(b, IDENTIFIER_EXPECTED);
            }
            int statementEnd = b.getCurrentOffset();
            b.advanceLexer(); // identifier
            completePPStatement(b, statementEnd);
        } finally {
            m.done(PREPROCESSOR_UNDEF_BLOCK);
        }
        return true;
    }

}
