package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;

public class MQL4EnumFieldElement extends MQL4PsiElement {

    public MQL4EnumFieldElement(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public String getFieldName() {
        ASTNode fieldNameElement = getNode().findChildByType(MQL4Elements.IDENTIFIER);
        return fieldNameElement == null ? "???" : fieldNameElement.getText();
    }

}
