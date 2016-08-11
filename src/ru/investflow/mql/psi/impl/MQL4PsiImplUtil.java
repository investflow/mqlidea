package ru.investflow.mql.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.MQL4Icons;
import ru.investflow.mql.psi.MQL4Identifier;
import ru.investflow.mql.psi.MQL4Reference;
import ru.investflow.mql.stub.MQL4IdentifierStub;

import javax.swing.Icon;

public class MQL4PsiImplUtil {

    // ------------- Identifier ------------------ //
    @NotNull
    public static String getName(@NotNull MQL4Identifier o) {
        MQL4IdentifierStub stub = o.getStub();
        if (stub != null) return StringUtil.notNullize(stub.getName());
        return o.getText();
    }

    @Nullable
    public static PsiElement getNameIdentifier(@NotNull MQL4Identifier o) {
        ASTNode keyNode = o.getNode();
        return keyNode != null ? keyNode.getPsi() : null;
    }

    @Nullable
    public static PsiElement setName(@NotNull MQL4Identifier o, @NotNull String newName) {
        PsiElement e = MQL4ElementFactory.createMQL4IdentifierFromText(o.getProject(), newName);
        if (e == null) return null;
        o.replace(e);
        return o;
    }

    @NotNull
    public static PsiReference getReference(@NotNull MQL4Identifier o) {
        return new MQL4Reference(o, TextRange.from(0, getName(o).length()));
    }


    public static PsiElement findParentOfType(PsiElement element, Class className) {
        if (className.isInstance(element)) {
            return element;
        } else {
            try {
                return findParentOfType(element.getParent(), className);
            } catch (Exception e) {
                return null;
            }
        }

    }

    private static String getParentDeclarationDescription(MQL4Identifier o) {
        return "";
    }

    @NotNull
    public static ItemPresentation getPresentation(final MQL4Identifier o) {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return o.getName() + getParentDeclarationDescription(o);
            }

            /**
             * This is needed to decipher between files when resolving multiple references.
             */
            @Nullable
            @Override
            public String getLocationString() {
                final PsiFile psiFile = o.getContainingFile();
                //todo: return psiFile instanceof MQL4File ? ((MQL4File) psiFile).getModuleOrFileName() : null;
                return null;
            }

            @Nullable
            @Override
            public Icon getIcon(boolean unused) {
                return MQL4Icons.FILE;
            }
        };
    }
}
