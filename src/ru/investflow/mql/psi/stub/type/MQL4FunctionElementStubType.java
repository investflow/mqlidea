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
import ru.investflow.mql.psi.impl.MQL4FunctionElement;
import ru.investflow.mql.psi.stub.MQL4FunctionElementStub;
import ru.investflow.mql.psi.stub.impl.MQL4FunctionElementStubImpl;
import ru.investflow.mql.util.LightTreeUtilEx;
import ru.investflow.mql.util.TextUtils;

import java.io.IOException;

public class MQL4FunctionElementStubType extends ILightStubElementType<MQL4FunctionElementStub, MQL4FunctionElement> {

    private final boolean declaration;

    public MQL4FunctionElementStubType(boolean declaration) {
        super(declaration ? "FUNCTION_DECLARATION" : "FUNCTION", MQL4Language.INSTANCE);
        this.declaration = declaration;
    }

    @Override
    public MQL4FunctionElementStub createStub(LighterAST tree, LighterASTNode node, StubElement parentStub) {
        // name
        LighterASTNode argsListStartNode = LightTreeUtil.firstChildOfType(tree, node, MQL4Elements.L_ROUND_BRACKET);
        LighterASTNode nameNode = argsListStartNode != null ? LightTreeUtilEx.prevSiblingOfType(tree, node, argsListStartNode, MQL4Elements.IDENTIFIER) : null;
        if (nameNode == null) {
            return null;
        }
        String name = ((LighterASTTokenNode) nameNode).getText().toString();

        // signature
        LighterASTNode argsListNode = LightTreeUtil.firstChildOfType(tree, node, MQL4Elements.FUNCTION_ARGS_LIST);
        if (argsListNode == null) {
            return null;
        }
        String signature = argsListNode.toString();

        return new MQL4FunctionElementStubImpl(parentStub, name, signature, declaration);
    }

    @Override
    public MQL4FunctionElement createPsi(@NotNull MQL4FunctionElementStub stub) {
        return new MQL4FunctionElement(stub);
    }

    @NotNull
    @Override
    public MQL4FunctionElementStub createStub(@NotNull MQL4FunctionElement psi, StubElement parentStub) {
        return new MQL4FunctionElementStubImpl(parentStub, psi.getFunctionName(), psi.getSignature(), psi.isDeclaration());
    }

    @NotNull
    @Override
    public String getExternalId() {
        return declaration ? "mqlidea.function-declaration" : "mqlidea.function";
    }

    @Override
    public void serialize(@NotNull MQL4FunctionElementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
        dataStream.writeUTFFast(stub.getSignature());
    }

    @NotNull
    @Override
    public MQL4FunctionElementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        StringRef nameRef = dataStream.readName();
        String signature = dataStream.readUTFFast();
        String name = nameRef == null ? MQL4FunctionElement.UNKNOWN_NAME : nameRef.toString();
        return new MQL4FunctionElementStubImpl(parentStub, name, signature, declaration);
    }

    @Override
    public void indexStub(@NotNull MQL4FunctionElementStub stub, @NotNull IndexSink sink) {
        String name = stub.getName();
        if (!MQL4FunctionElement.UNKNOWN_NAME.equals(name)) {
            sink.occurrence(MQL4IndexKeys.FUNCTION_NAME_INDEX_KEY, TextUtils.unescape(name));
        }
    }
}
