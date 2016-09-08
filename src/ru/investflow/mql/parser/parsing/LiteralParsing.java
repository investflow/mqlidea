package ru.investflow.mql.parser.parsing;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;

import static ru.investflow.mql.psi.MQL4TokenTypeSets.LITERALS;

public class LiteralParsing {

    public static boolean isLiteral(@NotNull PsiBuilder b) {
        IElementType t = b.getTokenType();
        return LITERALS.contains(t);
    }

    public static boolean parseLiteral(@NotNull PsiBuilder b) {
        if (isLiteral(b)) {
            b.advanceLexer();
            return true;
        }
        return false;
    }

}
