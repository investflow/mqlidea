package ru.investflow.mql.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4File;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class ResolveUtil {
    private ResolveUtil() {
    }

    public static boolean processChildren(@NotNull PsiElement element,
                                          @NotNull PsiScopeProcessor processor,
                                          @NotNull ResolveState substitutor,
                                          @Nullable PsiElement lastParent,
                                          @NotNull PsiElement place) {
        PsiElement run = lastParent == null ? element.getLastChild() : lastParent.getPrevSibling();
        while (run != null) {
            if (PsiTreeUtil.findCommonParent(place, run) != run && !run.processDeclarations(processor, substitutor, null, place)) {
                return false;
            }
            run = run.getPrevSibling();
        }
        return true;
    }

}
