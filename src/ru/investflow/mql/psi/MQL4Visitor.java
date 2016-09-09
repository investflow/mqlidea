package ru.investflow.mql.psi;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;

public class MQL4Visitor extends PsiElementVisitor {

    public void visitComment(@NotNull MQL4Comment o) {
        visitPsiElement(o);
    }

    public void visitLiteral(@NotNull MQL4Literal o) {
        visitPsiElement(o);
    }

    public void visitPreprocessorBlock(@NotNull MQL4PreprocessorBlock o) {
        visitPsiElement(o);
    }

    public void visitTopLevelDeclaration(@NotNull MQL4TopLevelDeclaration o) {
        visitPsiElement(o);
    }

    public void visitPsiElement(@NotNull PsiElement o) {
        visitElement(o);
    }

}
