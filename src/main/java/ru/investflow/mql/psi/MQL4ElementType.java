package ru.investflow.mql.psi;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.MQL4Language;

public class MQL4ElementType extends IElementType {

    public MQL4ElementType(@NotNull @NonNls String debugName) {
        super(debugName, MQL4Language.INSTANCE);
    }

    @Override
    public String toString() {
        return "MQL4ElementType." + super.toString();
    }
}
