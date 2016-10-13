package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;

public class CommentParsing implements MQL4Tokens {

    public static boolean parseComment(PsiBuilder b, int l) {
        if (b.getTokenType() != LINE_COMMENT && b.getTokenType() != BLOCK_COMMENT) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer();
        exit_section_(b, m, MQL4Elements.COMMENT, true);
        return true;
    }
}
