package ru.investflow.mql.psi.impl;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.stub.type.MQL4IdentifierStubElementType;

public class MQL4ElementTypeFactory {
    private MQL4ElementTypeFactory() {
    }

    public static IElementType factory(@NotNull String name) {
        if (name.equals("IDENTIFIER")) return new MQL4IdentifierStubElementType(name);
        throw new RuntimeException("Unknown element type: " + name);
    }
}
