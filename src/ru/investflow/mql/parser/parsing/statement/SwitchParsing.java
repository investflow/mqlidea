package ru.investflow.mql.parser.parsing.statement;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
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
            boolean ok = parseTokenOrFail(b, L_ROUND_BRACKET) // '('
                    && ExpressionParsing.parseExpressionOrFail(b, l, false) // expression
                    && parseTokenOrFail(b, R_ROUND_BRACKET) // ')'
                    && parseTokenOrFail(b, L_CURLY_BRACKET) // '{'
                    && parseSwitchCaseBlock(b, l + 1)
                    && parseTokenOrFail(b, R_CURLY_BRACKET); // '}'
            return true;
        } finally {
            exit_section_(b, m, SWITCH_BLOCK, true);
        }
    }

    private static TokenSet CASE_BLOCK_TERMINATORS = TokenSet.create(CASE_KEYWORD, DEFAULT_KEYWORD, R_CURLY_BRACKET);

    private static boolean parseSwitchCaseBlock(@NotNull PsiBuilder b, int l) {
        PsiBuilder.Marker m = enter_section_(b);
        try {
            while (b.getTokenType() == CASE_KEYWORD || b.getTokenType() == DEFAULT_KEYWORD) {
                boolean defaultBranch = b.getTokenType() == DEFAULT_KEYWORD;
                b.advanceLexer();
                boolean ok = (defaultBranch || parseCaseLiteralOrFail(b))
                        && parseTokenOrFail(b, COLON)
                        && (CASE_BLOCK_TERMINATORS.contains(b.getTokenType()) || parseCodeBlock(b, l + 1) || parseStatementOrFail(b, l + 1)); // '{}' or empty
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
