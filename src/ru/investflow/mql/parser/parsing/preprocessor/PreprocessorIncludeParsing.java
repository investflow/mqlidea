package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;

import java.util.Arrays;
import java.util.List;

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

    private static final List<IElementType> INCLUDE_STRING_LITERAL_MATCHER = Arrays.asList(LT, IDENTIFIER, GT);

    private static boolean parseIncludeStringLiteral(PsiBuilder b) {
        if (!ParsingUtils.matchSequenceOfElements(b, INCLUDE_STRING_LITERAL_MATCHER, 0)) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        try {
            ParsingUtils.advanceLexer(b, INCLUDE_STRING_LITERAL_MATCHER.size());
        } finally {
            m.done(MQL4Elements.INCLUDE_STRING_LITERAL);
        }
        return true;
    }
}
