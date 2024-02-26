package ru.investflow.mql.editor.folding;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;

public class EnumFoldingDescriptor extends FoldingDescriptor {
    public EnumFoldingDescriptor(ASTNode node) {
        super(node, node.getTextRange());
    }

    @Nullable
    @Override
    public String getPlaceholderText() {
        ASTNode typeName = getElement().findChildByType(MQL4Elements.IDENTIFIER);
        return "enum" + (typeName == null ? "" : " " + typeName.getText()) + " {...}";
    }
}
