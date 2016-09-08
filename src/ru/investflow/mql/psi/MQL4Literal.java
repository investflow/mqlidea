package ru.investflow.mql.psi;

import org.jetbrains.annotations.Nullable;

import com.intellij.psi.PsiElement;

public interface MQL4Literal extends PsiElement {

    @Nullable
    PsiElement getNumber();

    @Nullable
    PsiElement getString();

}
