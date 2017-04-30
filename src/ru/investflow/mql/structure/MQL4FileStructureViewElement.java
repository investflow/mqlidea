package ru.investflow.mql.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4File;
import ru.investflow.mql.psi.impl.MQL4EnumElement;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;
import ru.investflow.mql.structure.elements.MQL4StructureViewEnumElement;
import ru.investflow.mql.structure.elements.MQL4StructureViewFunctionElement;

import javax.swing.Icon;
import java.util.ArrayList;
import java.util.Collection;

public class MQL4FileStructureViewElement extends PsiTreeElementBase<MQL4File> {

    protected MQL4FileStructureViewElement(@NotNull MQL4File file) {
        super(file);
    }

    @NotNull
    public Collection<StructureViewTreeElement> getChildrenBase() {
        Collection<StructureViewTreeElement> elements = new ArrayList<>();
        PsiElement[] children = getFileElement().getChildren();
        for (PsiElement e : children) {
            if (e.getNode().getElementType() == MQL4Elements.FUNCTION_DEFINITION) {
                elements.add(new MQL4StructureViewFunctionElement((MQL4FunctionElement) e));
            } else if (e.getNode().getElementType() == MQL4Elements.ENUM_STATEMENT) {
                MQL4EnumElement enumElement = (MQL4EnumElement) e;
                if (!StringUtils.isEmpty(enumElement.getTypeName())) {
                    elements.add(new MQL4StructureViewEnumElement(enumElement));
                }

            }
        }
        return elements;
    }

    @NotNull
    public String getPresentableText() {
        return getFileElement().getName();
    }

    @NotNull
    private MQL4File getFileElement() {
        MQL4File file = getElement();
        assert file != null;
        return file;
    }

    @NotNull
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {

            public String getPresentableText() {
                return MQL4FileStructureViewElement.this.getPresentableText();
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                return getFileElement().getIcon(0);
            }
        };
    }
}
