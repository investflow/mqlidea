package ru.investflow.mql.structure.elements;

import com.intellij.icons.AllIcons;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4ClassElement;
import ru.investflow.mql.structure.MQL4StructureViewElement;

import javax.swing.Icon;
import java.util.Collection;

import static ru.investflow.mql.structure.MQL4FileStructureViewElement.toStructureViewElements;


public class MQL4StructureViewClassElement extends MQL4StructureViewElement<MQL4ClassElement> {

    public MQL4StructureViewClassElement(@NotNull MQL4ClassElement element) {
        super(element);
    }

    @NotNull
    public StructureViewTreeElement[] getChildren() {
        if (element.hasErrorElements()) {
            return new StructureViewTreeElement[0];
        }
        PsiElement[] children = element.getInnerBlockNode().getPsi().getChildren();
        Collection<StructureViewTreeElement> els = toStructureViewElements(children);
        return els.toArray(new StructureViewTreeElement[els.size()]);
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
                return element.isInterface() ? AllIcons.Nodes.Interface : AllIcons.Nodes.Class; // todo: custom icon for structs!
            }
        };
    }
}
