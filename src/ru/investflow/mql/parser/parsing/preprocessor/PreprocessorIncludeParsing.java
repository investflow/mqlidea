package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.nextTokenIs;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.assertNoLineBreaksInRange;

public class PreprocessorIncludeParsing implements MQL4Elements {

    public static boolean parseInclude(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseInclude")) {
            return false;
        }
        if (!nextTokenIs(b, INCLUDE_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        int startOffset = b.getCurrentOffset();
        b.advanceLexer();
        try {
            boolean r = assertNoLineBreaksInRange(b, startOffset, "#include argument is expected");
            if (!r) {
                return true;
            }
            IElementType tt = b.getTokenType();
            if (tt == STRING_LITERAL || tt == INCLUDE_STRING_LITERAL) {
                b.advanceLexer();
            } else {
                b.error("#include argument is expected");
            }
        } finally {
            exit_section_(b, m, PREPROCESSOR_INCLUDE_BLOCK, true);
        }

        return true;
    }

}
