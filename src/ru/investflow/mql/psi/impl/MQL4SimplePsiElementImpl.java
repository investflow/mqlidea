package ru.investflow.mql.psi.impl;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import ru.investflow.mql.psi.MQL4SimplePsiElement;
import ru.investflow.mql.psi.MQL4Visitor;

public class MQL4SimplePsiElementImpl extends ASTWrapperPsiElement implements MQL4SimplePsiElement {

    public MQL4SimplePsiElementImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof MQL4Visitor) {
            ((MQL4Visitor) visitor).visitSimplePsiElement(this);
        } else {
            super.accept(visitor);
        }
    }

}
