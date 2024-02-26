package ru.investflow.mql.psi.stub.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;
import ru.investflow.mql.psi.stub.MQL4FunctionElementStub;

import static ru.investflow.mql.psi.stub.MQL4StubElements.FUNCTION;
import static ru.investflow.mql.psi.stub.MQL4StubElements.FUNCTION_DECLARATION;

public class MQL4FunctionElementStubImpl extends StubBase<MQL4FunctionElement> implements MQL4FunctionElementStub {

    @NotNull
    private String name;
    @NotNull
    private String signature;

    private boolean declaration;

    public MQL4FunctionElementStubImpl(StubElement parent, @NotNull String name, @NotNull String signature, boolean declaration) {
        super(parent, declaration ? FUNCTION_DECLARATION : FUNCTION);
        this.name = name;
        this.signature = signature;
        this.declaration = declaration;
    }


    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getSignature() {
        return signature;
    }

    @Override
    public boolean isDeclaration() {
        return declaration;
    }
}
