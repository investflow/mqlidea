package ru.investflow.mql.index;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;

public class MQL4GotoDeclarationHandler implements GotoDeclarationHandler {

    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        if (sourceElement == null || editor == null) {
            return null;
        }
        String text = sourceElement.getText();
        Project project = editor.getProject();
        if (project == null) {
            return null;
        }
        Collection<MQL4FunctionElement> functions = MQL4FunctionNameIndex.getInstance().get(text, project, GlobalSearchScope.allScope(project));
        return functions.stream().filter(f -> f.getFunctionName().equals(text)).toArray(PsiElement[]::new);
    }

    @Nullable
    @Override
    public String getActionText(@NotNull DataContext context) {
        return null;
    }
}
