package ru.investflow.mql.psi;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.MQL4Language;

public class MQL4TokenType extends IElementType {
    @SuppressWarnings("unused")
    private final String debugName;

    public MQL4TokenType(@NotNull @NonNls String debugName) {
        super(debugName, MQL4Language.INSTANCE);
        this.debugName = debugName;
    }

    @Override
    public String toString() {
        return "MQL4TokenType." + super.toString();
    }
}
