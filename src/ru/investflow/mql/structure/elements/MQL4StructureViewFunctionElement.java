package ru.investflow.mql.structure.elements;

import com.intellij.navigation.ColoredItemPresentation;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Icons;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;
import ru.investflow.mql.structure.MQL4StructureViewElement;

import javax.swing.Icon;

import static ru.investflow.mql.util.TextUtils.abbreviate;
import static ru.investflow.mql.util.TextUtils.simplify;


public class MQL4StructureViewFunctionElement extends MQL4StructureViewElement<MQL4FunctionElement> {

    public MQL4StructureViewFunctionElement(@NotNull MQL4FunctionElement element) {
        super(element);
    }

    @NotNull
    public ItemPresentation getPresentation() {
        return new ColoredItemPresentation() {
            public TextAttributesKey getTextAttributesKey() {
                return null;
            }

            public String getPresentableText() {
                return element.getFunctionName() + "(" + abbreviate(simplify(element.getSignature()), 140) + ")";
            }

            public String getLocationString() {
                return null;
            }

            public Icon getIcon(boolean open) {
                boolean declaration = element.isDeclaration();
                PsiElement p = element.getParent();
                if (p != null) {
                    if (p.getNode().getElementType() == MQL4Elements.CLASS_INNER_BLOCK) {
                        return declaration ? MQL4Icons.MethodDeclaration : MQL4Icons.MethodDefinition;
                    }
                }
                return declaration ? MQL4Icons.FunctionDeclaration : MQL4Icons.FunctionDefinition;
            }
        };
    }
}
