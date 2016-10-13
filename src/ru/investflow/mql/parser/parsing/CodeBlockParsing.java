package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.parser.parsing.util.TokenAdvanceMode;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseStatement;

public class CodeBlockParsing {

    public static boolean parseCodeBlock(PsiBuilder b, int l) {
        if (b.getTokenType() != MQL4Tokens.LBRACE || !recursion_guard_(b, l, "parseCodeBlock")) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        try {
            b.advanceLexer(); // lbrace

            while (true) {
                IElementType t = b.getTokenType();
                if (t == MQL4Tokens.RBRACE) {
                    b.advanceLexer();
                    break;
                }
                boolean ok = parseCodeBlock(b, l + 1)
                        || parseStatement(b, l + 1);
                if (!ok) {
                    b.advanceLexer();
                    b.error("Valid MQL4 statement expected");
                    ParsingUtils.advanceLexerUntil(b, MQL4Tokens.RBRACE, TokenAdvanceMode.ADVANCE);
                    break;
                }
            }
        } finally {
            exit_section_(b, m, MQL4Elements.CODE_BLOCK, true);
        }
        return true;
    }
}
