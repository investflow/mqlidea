package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

public class MQL4PreprocessorPropertyBlock extends MQL4PsiElement {

    @Nullable
    public ASTNode keyNode;

    @Nullable
    public ASTNode valueNode;

    public MQL4PreprocessorPropertyBlock(@NotNull ASTNode node) {
        super(node);
    }

    public void sync() {
        boolean hasErrors = hasErrorElements();
        keyNode = hasErrors ? null : getNode().findChildByType(MQL4Elements.IDENTIFIER);
        valueNode = hasErrors ? null : getNode().findChildByType(MQL4TokenSets.LITERALS);
    }

    @Override
    public String toString() {
        return "#property";
    }
}
