package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.psi.MQL4Elements.CHAR_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.COLOR_CONSTANT_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.COLOR_STRING_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.DOUBLE_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.FALSE_KEYWORD;
import static ru.investflow.mql.psi.MQL4Elements.IDENTIFIER;
import static ru.investflow.mql.psi.MQL4Elements.INTEGER_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.STRING_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.TRUE_KEYWORD;

public class MQL4PreprocessorPropertyBlock extends MQL4PsiElement {

    public static final TokenSet VALUE_NODE_TYPE = TokenSet.create(
            IDENTIFIER,
            STRING_LITERAL,
            CHAR_LITERAL,
            COLOR_STRING_LITERAL,
            COLOR_CONSTANT_LITERAL,
            INTEGER_LITERAL,
            DOUBLE_LITERAL,
            TRUE_KEYWORD,
            FALSE_KEYWORD);

    @Nullable
    public ASTNode keyNode;

    @Nullable
    public ASTNode valueNode;

    public MQL4PreprocessorPropertyBlock(@NotNull ASTNode node) {
        super(node);
    }

    public void sync() {
        ASTNode node = getNode();
        keyNode = node.findChildByType(MQL4Elements.IDENTIFIER);
        valueNode = keyNode == null ? null : node.findChildByType(VALUE_NODE_TYPE, keyNode.getTreeNext());
    }

    @Override
    public String toString() {
        return "#property";
    }
}
