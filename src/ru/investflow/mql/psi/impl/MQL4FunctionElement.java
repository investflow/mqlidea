package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;

public class MQL4FunctionElement extends MQL4PsiElement {

    public MQL4FunctionElement(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public String getFunctionName() {
        return getFunctionNameElement().getText();
    }

    @NotNull
    public ASTNode getFunctionNameElement() {
        ASTNode nameElement = getNode().findChildByType(MQL4Elements.IDENTIFIER);
        assert nameElement != null;
        return nameElement;
    }
}
