package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;

public class ExpressionParsing {

    public static boolean parseExpression(PsiBuilder b, int l) {
        return LiteralParsing.parseLiteral(b);
    }

    public static boolean parseExpressionOrFail(PsiBuilder b, int l) {
        if (parseExpression(b, l)) {
            return true;
        }
        b.error("Expression expected");
        return false;
    }
}
