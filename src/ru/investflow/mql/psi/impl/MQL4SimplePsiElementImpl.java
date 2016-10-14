package ru.investflow.mql.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import ru.investflow.mql.psi.MQL4SimplePsiElement;

public class MQL4SimplePsiElementImpl extends ASTWrapperPsiElement implements MQL4SimplePsiElement {

    public MQL4SimplePsiElementImpl(ASTNode node) {
        super(node);
    }


}
