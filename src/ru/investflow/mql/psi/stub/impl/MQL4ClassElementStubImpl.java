package ru.investflow.mql.psi.stub.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4ClassElement;
import ru.investflow.mql.psi.stub.MQL4ClassElementStub;

public class MQL4ClassElementStubImpl extends StubBase<MQL4ClassElement> implements MQL4ClassElementStub {

    @NotNull
    private final String key;
    private final MQL4ClassElement.ClassType classType;

    public MQL4ClassElementStubImpl(@NotNull StubElement parent, @NotNull String key, @NotNull MQL4ClassElement.ClassType classType) {
        super(parent, MQL4Elements.CLASS);
        this.key = key;
        this.classType = classType;
    }

    @NotNull
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public MQL4ClassElement.ClassType getClassType() {
        return classType;
    }
}
