package ru.investflow.mql.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;

public abstract class MQL4StructureViewElement<T extends Navigatable> implements StructureViewTreeElement {

    @NotNull
    protected final T element;

    public MQL4StructureViewElement(@NotNull T element) {
        this.element = element;
    }

    public Object getValue() {
        return element;
    }

    public void navigate(boolean requestFocus) {
        element.navigate(requestFocus);
    }

    public boolean canNavigate() {
        return element.canNavigate();
    }

    public boolean canNavigateToSource() {
        return element.canNavigateToSource();
    }

    @NotNull
    public StructureViewTreeElement[] getChildren() {
        return EMPTY_ARRAY;
    }
}
