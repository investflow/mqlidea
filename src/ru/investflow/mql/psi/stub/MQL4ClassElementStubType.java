package ru.investflow.mql.psi.stub;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.LighterASTTokenNode;
import com.intellij.psi.impl.source.tree.LightTreeUtil;
import com.intellij.psi.stubs.ILightStubElementType;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.CharTable;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Language;
import ru.investflow.mql.index.MQL4ClassNameIndex;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4ClassElement;
import ru.investflow.mql.psi.stub.impl.MQL4ClassElementStubImpl;
import ru.investflow.mql.util.TextUtils;

import java.io.IOException;

public class MQL4ClassElementStubType extends ILightStubElementType<MQL4ClassElementStub, MQL4ClassElement> {

    public MQL4ClassElementStubType() {
        super("CLASS", MQL4Language.INSTANCE);
    }

    @Override
    public MQL4ClassElementStub createStub(LighterAST tree, LighterASTNode node, StubElement parentStub) {
        LighterASTNode keyNode = LightTreeUtil.firstChildOfType(tree, node, MQL4Elements.IDENTIFIER);
        String key = intern(tree.getCharTable(), keyNode);
        return new MQL4ClassElementStubImpl(parentStub, key);
    }

    public static String intern(@NotNull CharTable table, @NotNull LighterASTNode node) {
        assert node instanceof LighterASTTokenNode : node;
        return table.intern(((LighterASTTokenNode) node).getText()).toString();
    }

    @Override
    public MQL4ClassElement createPsi(@NotNull MQL4ClassElementStub stub) {
        return new MQL4ClassElement(stub);
    }

    @NotNull
    @Override
    public MQL4ClassElementStub createStub(@NotNull MQL4ClassElement psi, StubElement parentStub) {
        return new MQL4ClassElementStubImpl(parentStub, psi.getTypeName());
    }

    @NotNull
    @Override
    public String getExternalId() {
        return "mqlidea.class";
    }

    @Override
    public void serialize(@NotNull MQL4ClassElementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getKey());
    }

    @NotNull
    @Override
    public MQL4ClassElementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef ref = dataStream.readName();
        return new MQL4ClassElementStubImpl(parentStub, ref == null ? "???" : ref.getString());
    }

    @Override
    public void indexStub(@NotNull MQL4ClassElementStub stub, @NotNull IndexSink sink) {
        sink.occurrence(MQL4ClassNameIndex.KEY, TextUtils.unescape(stub.getKey()));
    }
}
