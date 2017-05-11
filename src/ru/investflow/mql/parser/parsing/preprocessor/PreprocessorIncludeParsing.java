package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.assertNoLineBreaksInRange;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.completePPStatement;

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
            int paramOffset = b.getCurrentOffset();
            if (tt != STRING_LITERAL && !parseIncludeStringLiteral(b)) {
                completePPStatement(b, b.getCurrentOffset(), errorMessage);
                return true;
            }
            if (tt == STRING_LITERAL) {
                b.advanceLexer(); // include parameter
            }
            completePPStatement(b, paramOffset);
        } finally {
            m.done(PREPROCESSOR_INCLUDE_BLOCK);
        }
        return true;
    }

    private static boolean parseIncludeStringLiteral(PsiBuilder b) {
        if (b.getTokenType() != LT) {
            return false;
        }
        b.advanceLexer(); // '<'
        while (b.getTokenType() != GT) {
            int startPos = b.getCurrentOffset();
            b.advanceLexer();
            if (ParsingUtils.containsEndOfLineOrFile(b, startPos)) {
                return false;
            }
        }
        b.advanceLexer(); // '>'
        return true;
    }
}
