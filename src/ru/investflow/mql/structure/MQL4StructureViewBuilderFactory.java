package ru.investflow.mql.structure;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4File;

public class MQL4StructureViewBuilderFactory implements PsiStructureViewFactory {
    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {
        return (fileEditor, project) -> new MQL4FileStructureViewComponent(project, (MQL4File) psiFile, fileEditor);
    }
}
