package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.parser.parsing.util.TokenAdvanceMode;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseStatement;

public class CodeBlockParsing implements MQL4Elements {

    public static boolean parseCodeBlockOrFail(PsiBuilder b, int l) {
        if (parseCodeBlock(b, l)) {
            return true;
        }
        b.error("Code block expected");
        return false;
    }

    public static boolean parseCodeBlock(PsiBuilder b, int l) {
        if (!ParsingUtils.nextTokenIs(b, l, "parseCodeBlock", L_CURLY_BRACKET)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        try {
            b.advanceLexer(); // '{'

            while (true) {
                IElementType t = b.getTokenType();
                if (t == R_CURLY_BRACKET) {
                    b.advanceLexer();
                    break;
                }
                boolean ok = parseCodeBlock(b, l + 1)
                        || parseStatement(b, l + 1);
                if (!ok) {
                    b.advanceLexer();
                    b.error("Valid MQL4 statement expected");
                    ParsingUtils.advanceLexerUntil(b, R_CURLY_BRACKET, TokenAdvanceMode.ADVANCE);
                    break;
                }
            }
        } finally {
            exit_section_(b, m, CODE_BLOCK, true);
        }
        return true;
    }
}
