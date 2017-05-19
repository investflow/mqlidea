package ru.investflow.mql.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;

import java.util.Collection;

import static ru.investflow.mql.psi.stub.MQL4StubElements.STUB_SCHEMA_VERSION;

public abstract class MQL4AbstractStringStubIndex<Psi extends PsiElement> extends StringStubIndexExtension<Psi> {

    @Override
    public int getVersion() {
        return STUB_SCHEMA_VERSION;
    }

    public Collection<String> getAllKeys(Project project) {
        return StubIndex.getInstance().getAllKeys(getKey(), project);
    }

}
