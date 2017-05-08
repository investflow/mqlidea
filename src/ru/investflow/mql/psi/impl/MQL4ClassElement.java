package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    public boolean isDeclaration() {
        return getInnerBlockNode() == null;
    }

    @Nullable
    public ASTNode getInnerBlockNode() {
        return getNode().findChildByType(MQL4Elements.CLASS_INNER_BLOCK);
    }
}
