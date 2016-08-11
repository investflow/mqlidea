package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4NamedElement;

public abstract class MQL4NamedStubbedPsiElementBase<T extends StubElement<?>> extends MQL4StubbedPsiElementBase<T> implements MQL4NamedElement {

    public MQL4NamedStubbedPsiElementBase(ASTNode node) {
        super(node);
    }

    public MQL4NamedStubbedPsiElementBase(@NotNull T stub, IStubElementType type) {
        super(stub, type);
    }
}
