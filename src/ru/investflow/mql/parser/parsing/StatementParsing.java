package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;

public class StatementParsing {

    public static boolean parseStatement(PsiBuilder b, int l) {
        return parseEmptyStatement(b, l);
    }

    private static boolean parseEmptyStatement(PsiBuilder b, int l) {
        IElementType t = b.getTokenType();
        if (t != MQL4Tokens.SEMICOLON) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer();
        exit_section_(b, m, MQL4Elements.EMPTY_STATEMENT, true);
        return true;
    }
}
