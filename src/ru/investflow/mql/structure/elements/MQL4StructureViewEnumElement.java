package ru.investflow.mql.structure.elements;

import com.intellij.icons.AllIcons;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4EnumElement;
import ru.investflow.mql.structure.MQL4StructureViewElement;

import javax.swing.Icon;


public class MQL4StructureViewEnumElement extends MQL4StructureViewElement<MQL4EnumElement> {

    public MQL4StructureViewEnumElement(@NotNull MQL4EnumElement element) {
        super(element);
    }

    @NotNull
    public StructureViewTreeElement[] getChildren() {
        if (element.hasErrorElements()) {
            return new StructureViewTreeElement[0];
        }
        return element.getFields().stream()
                .filter(fe -> !fe.hasErrorElements())
                .map(MQL4StructureViewEnumFieldElement::new)
                .toArray(StructureViewTreeElement[]::new);
    }

    @NotNull
    public ItemPresentation getPresentation() {
        return new ColoredItemPresentation() {
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }

            public String getPresentableText() {
                return element.getTypeName();
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                return AllIcons.Nodes.Enum;
            }
        };
    }
}
