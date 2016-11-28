package ru.investflow.mql.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4CompositeElement;

public class MQL4ElementsFactory implements MQL4Elements {

    public static PsiElement createElement(@NotNull ASTNode node) {
        return new MQL4CompositeElement(node);
    }
}
