package ru.investflow.mql.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4ClassElement;

import java.util.Collection;

public class MQL4ClassNameIndex extends StringStubIndexExtension<MQL4ClassElement> {

    public static final StubIndexKey<String, MQL4ClassElement> KEY = StubIndexKey.createIndexKey("mql4.className.index");

    private static final MQL4ClassNameIndex INSTANCE = new MQL4ClassNameIndex();


    public static MQL4ClassNameIndex getInstance() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public StubIndexKey<String, MQL4ClassElement> getKey() {
        return KEY;
    }

    @Override
    public Collection<MQL4ClassElement> get(@NotNull final String key, @NotNull final Project project, @NotNull final GlobalSearchScope scope) {
        return StubIndex.getElements(getKey(), key, project, scope, MQL4ClassElement.class);
    }

    public Collection<String> getAllKeys(Project project) {
        return StubIndex.getInstance().getAllKeys(getKey(), project);
    }

    @Override
    public int getVersion() {
        return 1;
    }
}