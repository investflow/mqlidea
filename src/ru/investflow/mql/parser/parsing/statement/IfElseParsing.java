package ru.investflow.mql.parser.parsing.statement;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static ru.investflow.mql.parser.parsing.CodeBlockParsing.parseCodeBlock;
import static ru.investflow.mql.parser.parsing.ExpressionParsing.parseExpressionOrFail;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseStatementOrFail;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.nextTokenIs;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;

public class IfElseParsing implements MQL4Elements {

    public static boolean parseIfElse(PsiBuilder b, int l) {
        if (!nextTokenIs(b, l, "parseIfElse", IF_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        try {
            while (true) {
                b.advanceLexer(); // 'if'
                boolean ok = parseTokenOrFail(b, LPARENTH) // '('
                        && parseExpressionOrFail(b, l + 1, false)
                        && parseTokenOrFail(b, RPARENTH) // ')'
                        && (parseCodeBlock(b, l + 1) || parseStatementOrFail(b, l + 1)); // '{}'
                if (!ok) {
                    break;
                }
                if (b.getTokenType() != ELSE_KEYWORD) {
                    break;
                }
                b.advanceLexer(); // else
                if (b.getTokenType() == IF_KEYWORD) {
                    continue;
                }
                //noinspection UnusedAssignment
                ok = parseCodeBlock(b, l + 1) || parseStatementOrFail(b, l + 1); // '{}'
                break;
            }
            return true;
        } finally {
            exit_section_(b, m, IF_ELSE_BLOCK, true);
        }
    }
}
