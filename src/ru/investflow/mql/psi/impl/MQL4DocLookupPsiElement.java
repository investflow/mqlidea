package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class MQL4DocLookupPsiElement extends MQL4PsiElement {

    @NotNull
    public final String text;

    public MQL4DocLookupPsiElement(@NotNull String text, @NotNull ASTNode node) {
        super(node);
        this.text = text;
    }

    @NotNull
    @Override
    public String getText() {
        return text;
    }
}
