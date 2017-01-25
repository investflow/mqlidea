package ru.investflow.mql.inspection;

import com.intellij.codeInspection.CustomSuppressableInspectionTool;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.SuppressIntentionAction;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.SmartList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4PreprocessorPropertyBlock;

import java.util.List;

public class PreprocessorPropertyInspection extends LocalInspectionTool implements CustomSuppressableInspectionTool {

    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull final InspectionManager manager, final boolean isOnTheFly) {
        final PsiElement[] elements = file.getChildren();
        final List<ProblemDescriptor> descriptors = new SmartList<>();

        for (PsiElement e : elements) {
            ProgressManager.checkCanceled();
            if (e.getNode().getElementType() != MQL4Elements.PREPROCESSOR_PROPERTY_BLOCK) {
                continue;
            }
            MQL4PreprocessorPropertyBlock block = (MQL4PreprocessorPropertyBlock) e;
            if (block.hasParsingErrors || block.keyNode == null) {
                continue;
            }
            ProblemDescriptor d = validate(manager, block.keyNode, block.valueNode);
            if (d != null) {
                descriptors.add(d);
            }
        }
        return descriptors.toArray(new ProblemDescriptor[0]);
    }

    @Nullable
    private static ProblemDescriptor validate(InspectionManager manager, @NotNull ASTNode keyNode, @Nullable ASTNode valueNode) {
        if (keyNode.getText().equals("strict") && valueNode != null) {
            return manager.createProblemDescriptor(valueNode.getPsi(), valueNode.getPsi(),
                    "No value is needed for 'strict' property",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true, new RemovePropertyValueFix());
        }
        return null;
    }

    @Nullable
    @Override
    public SuppressIntentionAction[] getSuppressActions(@Nullable PsiElement psiElement) {
        return null;
    }

    private static class RemovePropertyValueFix implements LocalQuickFix {

        @NotNull
        public String getName() {
            return "Remove value";
        }

        @NotNull
        public String getFamilyName() {
            return getName();
        }

        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement el = descriptor.getPsiElement();
            TextRange rangeToRemove = extendLeft(el, MQL4Elements.WHITE_SPACE);
            Document document = PsiDocumentManager.getInstance(project).getDocument(el.getContainingFile());
            assert document != null;
            document.deleteString(rangeToRemove.getStartOffset(), rangeToRemove.getEndOffset());

        }
    }

    @NotNull
    private static TextRange extendLeft(@NotNull PsiElement el, @NotNull IElementType siblingType) {
        int endOffset = el.getTextRange().getEndOffset();
        while (true) {
            PsiElement prev = el.getPrevSibling();
            if (prev == null || (prev.getNode().getElementType() != siblingType)) {
                break;
            }
            el = prev;
        }
        return new TextRange(el.getTextOffset(), endOffset);
    }
}