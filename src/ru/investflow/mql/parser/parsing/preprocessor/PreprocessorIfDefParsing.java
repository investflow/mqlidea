package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.assertNoLineBreaksInRange;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.IDENTIFIER_EXPECTED;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.advanceWithError;
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
            PreprocessorParsing.completePPLineStatement(b, statementEnd);
        } finally {
            m.done(PREPROCESSOR_UNDEF_BLOCK);
        }
        return true;
    }

    private static final TokenSet VALID_DEFINE_KEY_TYPES = TokenSet.create(IDENTIFIER, COLOR_CONSTANT_LITERAL);

    public static boolean parseDefine(PsiBuilder b) {
        if (b.getTokenType() != DEFINE_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        int startOffset = b.getCurrentOffset();
        b.advanceLexer(); // '#define' keyword
        try {
            if (!assertNoLineBreaksInRange(b, startOffset, ParsingErrors.IDENTIFIER_EXPECTED)) {
                return true;
            }
            int identifierOffset = b.getCurrentOffset();
            if (!VALID_DEFINE_KEY_TYPES.contains(b.getTokenType())) {
                advanceWithError(b, IDENTIFIER_EXPECTED);
            } else {
                b.advanceLexer(); // identifier
            }
            // skip until end of line and all escaped lines
            PreprocessorParsing.completePPMultiLineStatement(b, identifierOffset);
        } finally {
            m.done(PREPROCESSOR_DEFINE_BLOCK);
        }
        return true;
    }

}
