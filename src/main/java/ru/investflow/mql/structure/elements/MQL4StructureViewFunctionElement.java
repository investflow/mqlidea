package ru.investflow.mql.structure.elements;

import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;
import ru.investflow.mql.structure.MQL4StructureViewElement;


public class MQL4StructureViewFunctionElement extends MQL4StructureViewElement<MQL4FunctionElement> {

    public MQL4StructureViewFunctionElement(@NotNull MQL4FunctionElement element) {
        super(element);
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        return element.getPresentation();
    }
}
