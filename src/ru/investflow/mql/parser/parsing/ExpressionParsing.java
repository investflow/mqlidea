package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.MQL4Parser;

public class ExpressionParsing {

    public static boolean parseExpression(PsiBuilder b, int l) {
        return MQL4Parser.parseIdentifier(b)
                || LiteralParsing.parseLiteral(b);
    }

    public static boolean parseExpressionOrFail(PsiBuilder b, int l) {
        if (parseExpression(b, l)) {
            return true;
        }
        b.error("Expression expected");
        return false;
    }
}
