package ru.investflow.mql.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class MQL4CompositeElement extends ASTWrapperPsiElement {

    public MQL4CompositeElement(@NotNull ASTNode node) {
        super(node);
    }

}
