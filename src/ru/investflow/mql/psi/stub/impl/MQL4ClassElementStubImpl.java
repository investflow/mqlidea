package ru.investflow.mql.psi.stub.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4ClassElement;
import ru.investflow.mql.psi.stub.MQL4ClassElementStub;
import ru.investflow.mql.psi.stub.MQL4StubElementTypes;

public class MQL4ClassElementStubImpl extends StubBase<MQL4ClassElement> implements MQL4ClassElementStub {

    @NotNull
    private final String key;

    public MQL4ClassElementStubImpl(StubElement parent, @NotNull String key) {
        super(parent, MQL4StubElementTypes.CLASS);
        this.key = key;
    }

    @NotNull
    @Override
    public String getKey() {
        return key;
    }
}
