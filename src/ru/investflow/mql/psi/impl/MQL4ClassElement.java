package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;

public class MQL4ClassElement extends MQL4PsiElement {

    public MQL4ClassElement(@NotNull ASTNode node) {
        super(node);
    }

    public boolean isStruct() {
        return getNode().getFirstChildNode().getElementType() == MQL4Elements.STRUCT_KEYWORD;
    }

    public boolean isClass() {
        return getNode().getFirstChildNode().getElementType() == MQL4Elements.CLASS_KEYWORD;
    }

    public boolean isInterface() {
        return getNode().getFirstChildNode().getElementType() == MQL4Elements.INTERFACE_KEYWORD;
    }

    @NotNull
    public String getTypeName() {
        ASTNode fieldNameNode = getNode().findChildByType(MQL4Elements.IDENTIFIER);
        return fieldNameNode == null ? "<unknown>" : fieldNameNode.getText();
    }

    @NotNull
    public ASTNode getInnerBlockNode() {
        ASTNode node = getNode().findChildByType(MQL4Elements.CLASS_INNER_BLOCK);
        assert node != null;
        return node;
    }
}
