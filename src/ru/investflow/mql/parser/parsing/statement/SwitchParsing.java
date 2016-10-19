package ru.investflow.mql.parser.parsing.statement;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.parsing.ExpressionParsing;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static ru.investflow.mql.parser.parsing.CodeBlockParsing.parseCodeBlock;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseStatementOrFail;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.nextTokenIs;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;

public class SwitchParsing implements MQL4Elements {

    public static boolean parseSwitch(@NotNull PsiBuilder b, int l) {
        if (!nextTokenIs(b, l, "parseSwitch", SWITCH_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        try {
            b.advanceLexer(); // 'switch'
            //noinspection unused
            boolean ok = parseTokenOrFail(b, LBRACE) // '('
                    && ExpressionParsing.parseExpressionOrFail(b, l, false) // expression
                    && parseTokenOrFail(b, RBRACE) // ')'
                    && parseTokenOrFail(b, LBRACKET) // '{'
                    && parseSwitchCaseBlock(b, l + 1)
                    && parseTokenOrFail(b, RBRACKET); // '}'
            return true;
        } finally {
            exit_section_(b, m, SWITCH_BLOCK, true);
        }
    }

    private static boolean parseSwitchCaseBlock(@NotNull PsiBuilder b, int l) {
        PsiBuilder.Marker m = enter_section_(b);
        try {
            while (b.getTokenType() == CASE_KEYWORD || b.getTokenType() == DEFAULT_KEYWORD) {
                boolean defaultBranch = b.getTokenType() == DEFAULT_KEYWORD;
                b.advanceLexer();
                boolean ok = (defaultBranch || parseCaseLiteralOrFail(b))
                        && parseTokenOrFail(b, COLON)
                        && (parseCodeBlock(b, l + 1) || parseStatementOrFail(b, l + 1)); // '{}'
                if (!ok) {
                    return false;
                }
            }
        } finally {
            exit_section_(b, m, SWITCH_CASE_BLOCK, true);
        }
        return true;
    }

    private static boolean parseCaseLiteralOrFail(@NotNull PsiBuilder b) {
        if (b.getTokenType() == INTEGER_LITERAL || b.getTokenType() == CHAR_LITERAL) {
            b.advanceLexer();
            return true;
        }
        b.error("Integer or char literal is expected");
        return false;
    }

}
