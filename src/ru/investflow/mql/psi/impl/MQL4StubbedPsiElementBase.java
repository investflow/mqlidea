package ru.investflow.mql.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import ru.investflow.mql.psi.MQL4CompositeElement;

public abstract class MQL4StubbedPsiElementBase<T extends StubElement<?>> extends StubBasedPsiElementBase<T> implements MQL4CompositeElement {
    public MQL4StubbedPsiElementBase(T stub, IStubElementType nodeType) {
        super(stub, nodeType);
    }

    public MQL4StubbedPsiElementBase(ASTNode node) {
        super(node);
    }

    @Override
    public String toString() {
        return getElementType().toString();
    }
}