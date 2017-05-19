package ru.investflow.mql.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;

import java.util.Collection;

public class MQL4FunctionNameIndex extends MQL4AbstractStringStubIndex<MQL4FunctionElement> {

    private static final MQL4FunctionNameIndex INSTANCE = new MQL4FunctionNameIndex();

    public static MQL4FunctionNameIndex getInstance() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public StubIndexKey<String, MQL4FunctionElement> getKey() {
        return MQL4IndexKeys.FUNCTION_NAME_INDEX_KEY;
    }

    @Override
    public Collection<MQL4FunctionElement> get(@NotNull String key, @NotNull Project project, @NotNull GlobalSearchScope scope) {
        return StubIndex.getElements(getKey(), key, project, scope, MQL4FunctionElement.class);
    }

}