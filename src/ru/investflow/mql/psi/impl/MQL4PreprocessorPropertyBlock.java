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
        if (!hasParsingErrors) {
            init(node);
        }
    }

    private void init(@NotNull ASTNode node) {
        keyNode = node.findChildByType(MQL4Elements.IDENTIFIER);
        valueNode = node.findChildByType(MQL4TokenSets.LITERALS);
    }

    @Override
    public String toString() {
        return "#property";
    }
}
