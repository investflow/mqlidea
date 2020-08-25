package ru.investflow.mql.psi.stub.type;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.ILightStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Language;
import ru.investflow.mql.index.MQL4IndexKeys;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;
import ru.investflow.mql.psi.impl.MQL4ClassElement;
import ru.investflow.mql.psi.impl.MQL4ClassElement.ClassType;
import ru.investflow.mql.psi.stub.MQL4ClassElementStub;
import ru.investflow.mql.psi.stub.impl.MQL4ClassElementStubImpl;
import ru.investflow.mql.util.TextUtils;

import java.io.IOException;

public class MQL4ClassElementStubType extends ILightStubElementType<MQL4ClassElementStub, MQL4ClassElement> {

    public MQL4ClassElementStubType() {
        super("CLASS", MQL4Language.INSTANCE);
    }

    @NotNull
    @Override
    public MQL4ClassElementStub createStub(@NotNull LighterAST tree, @NotNull LighterASTNode node, @NotNull StubElement parentStub) {
        LighterASTNode keyNode = LightTreeUtil.firstChildOfType(tree, node, MQL4Elements.IDENTIFIER);
        if (keyNode == null) {
            //TODO: fix
            return null;
        }
        String key = ((LighterASTTokenNode) keyNode).getText().toString();

        LighterASTNode typeNode = LightTreeUtil.firstChildOfType(tree, node, MQL4TokenSets.CLASS_STRUCT_INTERFACE);
        ClassType classType = ClassType.fromTokenType(typeNode == null ? MQL4Elements.CLASS_KEYWORD : typeNode.getTokenType());

        return new MQL4ClassElementStubImpl(parentStub, key, classType);
    }

    @Override
    public MQL4ClassElement createPsi(@NotNull MQL4ClassElementStub stub) {
        return new MQL4ClassElement(stub);
    }

    @NotNull
    @Override
    public MQL4ClassElementStub createStub(@NotNull MQL4ClassElement psi, StubElement parentStub) {
        return new MQL4ClassElementStubImpl(parentStub, psi.getTypeName(), psi.getClassType());
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "mqlidea.class";
    }

    @Override
    public void serialize(@NotNull MQL4ClassElementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        dataStream.writeName(stub.getClassType().serialize());
    }

    @NotNull
    @Override
    public MQL4ClassElementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef ref = dataStream.readName();
        StringRef classTypeRef = dataStream.readName();
        String name = ref == null ? MQL4ClassElement.UNKNOWN_NAME : ref.getString();
        ClassType classType = classTypeRef == null ? ClassType.Class : ClassType.deserialize(classTypeRef.getString());
        return new MQL4ClassElementStubImpl(parentStub, name, classType);
    }

    @Override
    public void indexStub(@NotNull MQL4ClassElementStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (!MQL4ClassElement.UNKNOWN_NAME.equals(name)) {
            sink.occurrence(MQL4IndexKeys.CLASS_NAME_INDEX_KEY, TextUtils.unescape(stub.getName()));
        }
    }
}
