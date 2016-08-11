package ru.investflow.mql.stub.type;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Language;
import ru.investflow.mql.psi.MQL4CompositeElement;

public abstract class MQL4StubElementType<S extends StubElement<T>, T extends MQL4CompositeElement> extends IStubElementType<S, T> {
    public MQL4StubElementType(String debugName) {
        super(debugName, MQL4Language.INSTANCE);
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "d." + super.toString();
    }
}