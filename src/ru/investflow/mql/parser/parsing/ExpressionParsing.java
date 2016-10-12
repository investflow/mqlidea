package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;

public class ExpressionParsing {

    public static boolean parseExpression(PsiBuilder b, int l) {
        //todo:
        return LiteralParsing.parseLiteral(b);
    }

    public static boolean parseExpressionOrFail(PsiBuilder b, int l) {
        boolean ok = parseExpression(b, l);
        if (!ok) {
            b.error("Expression expected");
        }
        return ok;
    }
}
