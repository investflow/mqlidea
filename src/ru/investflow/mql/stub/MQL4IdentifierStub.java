package ru.investflow.mql.stub;


import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import ru.investflow.mql.psi.MQL4Identifier;

public class MQL4IdentifierStub extends NamedStubBase<MQL4Identifier> {
    public MQL4IdentifierStub(StubElement parent, IStubElementType elementType, StringRef name) {
        super(parent, elementType, name);
    }

    public MQL4IdentifierStub(StubElement parent, IStubElementType elementType, String name) {
        super(parent, elementType, name);
    }
}
