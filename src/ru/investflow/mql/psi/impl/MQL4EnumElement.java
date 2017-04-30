package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MQL4EnumElement extends MQL4PsiElement {

    public MQL4EnumElement(@NotNull ASTNode node) {
        super(node);
    }

    @Nullable
    public String getTypeName() {
        ASTNode typeNameElement = getTypeNameNode();
        return typeNameElement == null ? null : typeNameElement.getText();
    }

    @Nullable
    public ASTNode getTypeNameNode() {
        return getNode().findChildByType(MQL4Elements.IDENTIFIER);
    }

    public List<MQL4EnumFieldElement> getFields() {
        PsiElement list = findChildByType(MQL4Elements.ENUM_FIELDS_LIST);
        if (list == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(list.getChildren()).map(e -> (MQL4EnumFieldElement) e).collect(Collectors.toList());
    }
}
