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
        return getFunctionNameNode().getText();
    }

    @NotNull
    public ASTNode getFunctionNameNode() {
        ASTNode nameNode = getNode().findChildByType(MQL4Elements.IDENTIFIER);
        assert nameNode != null;
        return nameNode;
    }

    public boolean isDeclaration() {
        return getNode().getElementType() == MQL4Elements.FUNCTION_DECLARATION;
    }
}
