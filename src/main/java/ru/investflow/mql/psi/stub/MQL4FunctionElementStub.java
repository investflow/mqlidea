package ru.investflow.mql.psi.stub;

import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;

public interface MQL4FunctionElementStub extends StubElement<MQL4FunctionElement> {

    @NotNull
    String getName();

    @NotNull
    String getSignature();

    boolean isDeclaration();
}

