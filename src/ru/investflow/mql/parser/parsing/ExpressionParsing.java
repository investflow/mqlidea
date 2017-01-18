package ru.investflow.mql.parser.parsing;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.MQL4Parser;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;

public class ExpressionParsing implements MQL4Elements {

    public static boolean parseExpression(@NotNull PsiBuilder b, int l) {
        return MQL4Parser.parseIdentifier(b)
                || LiteralParsing.parseLiteral(b)
                || parseSizeOf(b, l);
    }

    public static boolean parseExpressionOrFail(@NotNull PsiBuilder b, int l) {
        return parseExpressionOrFail(b, l, false);
    }

    //TODO: review all callers that fail on empty expressions during parsing
    public static boolean parseExpressionOrFail(@NotNull PsiBuilder b, int l, boolean allowEmpty) {
        if (allowEmpty && b.getTokenType() == SEMICOLON) {
            return true;
        }
        if (parseExpression(b, l)) {
            return true;
        }
        b.error("Expression expected");
        return false;
    }

    public static boolean parseSizeOf(@NotNull PsiBuilder b, int l) {
        if (!ParsingUtils.nextTokenIs(b, l, "parseSizeOf", MQL4Elements.SIZEOF_KEYWORD)) {
            return false;
        }
        b.advanceLexer(); // 'sizeof'
        return parseTokenOrFail(b, L_ROUND_BRACKET) // '('
                && parseTokenOrFail(b, MQL4Elements.IDENTIFIER)
                && parseTokenOrFail(b, R_ROUND_BRACKET); // ')'
    }
}
