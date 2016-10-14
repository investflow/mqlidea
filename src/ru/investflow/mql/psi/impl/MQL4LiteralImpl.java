package ru.investflow.mql.psi.impl;

import org.jetbrains.annotations.Nullable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Literal;


public class MQL4LiteralImpl extends ASTWrapperPsiElement implements MQL4Literal,MQL4Elements {

    public MQL4LiteralImpl(ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public PsiElement getNumber() {
        return findChildByType(INTEGER_LITERAL);
    }

    @Override
    @Nullable
    public PsiElement getString() {
        return findChildByType(STRING_LITERAL);
    }

}
