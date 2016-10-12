package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.StatementParsing.parseStatement;

public class CodeBlockParsing {

    public static boolean parseBlock(PsiBuilder b, int l) {
        if (b.getTokenType() != MQL4Tokens.LBRACE) {
            return false;
        }
        if (!recursion_guard_(b, l, "code-block")) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer();

        boolean res;
        do {
            IElementType t = b.getTokenType();
            if (t == MQL4Tokens.RBRACE) {
                b.advanceLexer();
                break;
            }
            res = parseBlock(b, l + 1) || parseStatement(b, l + 1);
        } while (res);

        exit_section_(b, m, MQL4Elements.CODE_BLOCK, true);
        return true;
    }
}
