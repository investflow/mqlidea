package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4TokenSets;

public class LiteralParsing {

    public static boolean parseLiteral(@NotNull PsiBuilder b) {
        if (MQL4TokenSets.LITERALS.contains((IElementType) b)) {
            b.advanceLexer();
            return true;
        }
        return false;
    }

}
