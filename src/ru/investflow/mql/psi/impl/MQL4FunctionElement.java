package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.util.ASTUtils;

public class MQL4FunctionElement extends MQL4PsiElement {

    public MQL4FunctionElement(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    public String getFunctionName() {
        ASTNode nameNode = getFunctionNameNode();
        ASTNode prevNode = ASTUtils.getPrevIgnoreCommentsAndWs(nameNode);
        return (prevNode != null && prevNode.getElementType() == MQL4Elements.TILDA ? "~" : "") + nameNode.getText();
    }

    @NotNull
    public ASTNode getFunctionNameNode() {
        ASTNode node = getNode();
        ASTNode argsListStartNode = node.findChildByType(MQL4Elements.L_ROUND_BRACKET);
        assert argsListStartNode != null;
        ASTNode nameNode = ASTUtils.findLastPrevByType(argsListStartNode, MQL4Elements.IDENTIFIER);
        assert nameNode != null;
        return nameNode;
    }

    public boolean isDeclaration() {
        return getNode().getElementType() == MQL4Elements.FUNCTION_DECLARATION;
    }
}
