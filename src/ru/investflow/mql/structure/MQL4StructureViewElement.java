package ru.investflow.mql.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;

import javax.swing.Icon;

public class MQL4StructureViewElement implements StructureViewTreeElement {

    @NotNull
    private final MQL4FunctionElement element;

    public MQL4StructureViewElement(@NotNull MQL4FunctionElement element) {
        this.element = element;
    }

    public Object getValue() {
        return element.getFunctionName();
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

    @NotNull
    public ItemPresentation getPresentation() {
        return new ColoredItemPresentation() {
            @Nullable
            @Override
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }

            public String getPresentableText() {
                return element.getFunctionName();
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                return null;
            }
        };
    }
}
