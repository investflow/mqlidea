package ru.investflow.mql.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.MQL4Icons;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.stub.MQL4ClassElementStub;
import ru.investflow.mql.psi.stub.MQL4StubElementTypes;

import javax.swing.Icon;

public class MQL4ClassElement extends StubBasedPsiElementBase<MQL4ClassElementStub> {


    public MQL4ClassElement(@NotNull ASTNode node) {
        super(node);
    }

    public MQL4ClassElement(@NotNull MQL4ClassElementStub stub) {
        super(stub, MQL4StubElementTypes.CLASS);
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

    @NotNull
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            public String getPresentableText() {
                return getTypeName();
            }

            public String getLocationString() {
                return getContainingFile().getName();
            }

            public Icon getIcon(boolean open) {
                return isInterface() ? MQL4Icons.Interface : isStruct() ? MQL4Icons.Struct : MQL4Icons.Class;
            }
        };
    }

    @Override
    public String getName() {
        return getTypeName();
    }
}
