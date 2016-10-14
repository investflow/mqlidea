package ru.investflow.mql.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.impl.MQL4LiteralImpl;
import ru.investflow.mql.psi.impl.MQL4SimplePsiElementImpl;

public class MQL4ElementFactory implements MQL4Elements {

    public static PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();
        if (type == INTEGER_LITERAL || type == CHAR_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == INCLUDE_STRING_LITERAL) {
            return new MQL4LiteralImpl(node);
        }
        return new MQL4SimplePsiElementImpl(node);
    }
}
