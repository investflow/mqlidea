package ru.investflow.mql.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public class MQL4PsiElement extends ASTWrapperPsiElement implements Navigatable {

    public MQL4PsiElement(@NotNull ASTNode node) {
        super(node);
    }

}
