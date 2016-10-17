package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.MQL4Parser;
import ru.investflow.mql.psi.MQL4Elements;

public class ExpressionParsing implements MQL4Elements {

    public static boolean parseExpression(PsiBuilder b, int l) {
        return MQL4Parser.parseIdentifier(b)
                || LiteralParsing.parseLiteral(b);
    }

    public static boolean parseExpressionOrFail(PsiBuilder b, int l) {
        return parseExpressionOrFail(b, l, false);
    }

    public static boolean parseExpressionOrFail(PsiBuilder b, int l, boolean allowEmpty) {
        if (allowEmpty && b.getTokenType() == SEMICOLON) {
            return true;
        }
        if (parseExpression(b, l)) {
            return true;
        }
        b.error("Expression expected");
        return false;
    }
}
