
package ru.investflow.mql.structure.elements;

import com.intellij.icons.AllIcons;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4EnumFieldElement;
import ru.investflow.mql.structure.MQL4StructureViewElement;

import javax.swing.Icon;


public class MQL4StructureViewEnumFieldElement extends MQL4StructureViewElement<MQL4EnumFieldElement> {

    public MQL4StructureViewEnumFieldElement(@NotNull MQL4EnumFieldElement element) {
        super(element);
    }

    @NotNull
    public ItemPresentation getPresentation() {
        return new ColoredItemPresentation() {
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }

            public String getPresentableText() {
                return element.getFieldName();
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                return AllIcons.Nodes.Field;
            }
        };
    }
}
