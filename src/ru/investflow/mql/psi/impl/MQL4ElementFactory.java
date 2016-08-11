package ru.investflow.mql.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.MQL4FileType;
import ru.investflow.mql.psi.MQL4File;
import ru.investflow.mql.psi.MQL4Identifier;

/**
 * Performs creation of element types.
 */
public class MQL4ElementFactory {

    /**
     * Takes a name and returns a Psi node of that name, or null.
     */
    @Nullable
    public static MQL4Identifier createMQL4IdentifierFromText(@NotNull Project project, @NotNull String name) {
        PsiElement e = createExpressionFromText(project, name + "uniq = " + name).getFirstChild();
        if (e instanceof MQL4Identifier) return (MQL4Identifier) e;
        return null;
    }

    /*
     * Takes an expression in text and returns a Psi tree of that program.
     */
    @NotNull
    public static PsiElement createExpressionFromText(@NotNull Project project, @NotNull String name) {
        MQL4File fileFromText = createFileFromText(project, name);
        PsiElement rhs = fileFromText.getFirstChild().getFirstChild().getLastChild();
        return rhs.getLastChild().getLastChild().getLastChild();
    }

    /**
     * Create a file containing text.
     */
    @NotNull
    public static MQL4File createFileFromText(@NotNull Project project, @NotNull String text) {
        return (MQL4File) PsiFileFactory.getInstance(project).createFileFromText("A.hs", MQL4FileType.INSTANCE, text);
    }

}
