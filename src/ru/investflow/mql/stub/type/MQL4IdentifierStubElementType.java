package ru.investflow.mql.stub.type;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Identifier;
import ru.investflow.mql.psi.impl.MQL4IdentifierImpl;
import ru.investflow.mql.stub.MQL4IdentifierStub;

import java.io.IOException;

public class MQL4IdentifierStubElementType extends MQL4NamedStubElementType<MQL4IdentifierStub, MQL4Identifier> {

    public MQL4IdentifierStubElementType(String debugName) {
        super(debugName);
    }

    @Override
    public MQL4Identifier createPsi(@NotNull MQL4IdentifierStub stub) {
        return new MQL4IdentifierImpl(stub, this);
    }

    @Override
    public boolean shouldCreateStub(ASTNode node) {
        return false;//TODO: MQL4Util.definitionNode(node);
    }

    @Override
    public MQL4IdentifierStub createStub(@NotNull MQL4Identifier psi, StubElement parentStub) {
        return new MQL4IdentifierStub(parentStub, this, psi.getName());
    }

    @Override
    public void serialize(@NotNull MQL4IdentifierStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
    }

    @NotNull
    @Override
    public MQL4IdentifierStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new MQL4IdentifierStub(parentStub, this, dataStream.readName());
    }
}