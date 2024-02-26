package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class MQL4PreprocessorIncludeBlock extends MQL4PsiElement {

    public MQL4PreprocessorIncludeBlock(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String toString() {
        return "#include";
    }
}
