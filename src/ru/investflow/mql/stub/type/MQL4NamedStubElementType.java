package ru.investflow.mql.stub.type;

import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.NamedStubBase;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4NamedElement;
import ru.investflow.mql.stub.index.MQL4AllNameIndex;

public abstract class MQL4NamedStubElementType<S extends NamedStubBase<T>, T extends MQL4NamedElement> extends MQL4StubElementType<S, T> {
    public MQL4NamedStubElementType(String debugName) {
        super(debugName);
    }

    @Override
    public void indexStub(@NotNull S stub, @NotNull IndexSink sink) {
        final String name = stub.getName();
        if (name != null) {
            sink.occurrence(MQL4AllNameIndex.KEY, name);
        }
    }
}
