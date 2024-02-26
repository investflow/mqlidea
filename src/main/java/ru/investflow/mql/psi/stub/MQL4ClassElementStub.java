package ru.investflow.mql.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4ClassElement;

public interface MQL4ClassElementStub extends StubElement<MQL4ClassElement> {

    @NotNull
    String getName();

    @NotNull
    MQL4ClassElement.ClassType getClassType();
}

