package ru.investflow.mql.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiErrorElement;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class MQL4PsiElement extends ASTWrapperPsiElement {

    public MQL4PsiElement(@NotNull ASTNode node) {
        super(node);
    }

    public boolean hasErrorElements() {
        return Stream.of(getChildren()).anyMatch(p -> p instanceof PsiErrorElement);
    }
}
