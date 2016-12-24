package ru.investflow.mql.psi;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import ru.investflow.mql.MQL4FileType;
import ru.investflow.mql.MQL4Language;

public class MQL4File extends PsiFileBase {
    public MQL4File(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, MQL4Language.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return MQL4FileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "MQL4 File";
    }
}