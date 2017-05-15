package ru.investflow.mql.index;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.HashSet;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4ClassElement;

import java.util.Collection;
import java.util.Set;

public class MQL4GotoClassContributor implements ChooseByNameContributor {
    @NotNull
    public String[] getNames(final Project project, final boolean includeNonProjectItems) {
        Set<String> results = new HashSet<>();
        MQL4ClassNameIndex index = MQL4ClassNameIndex.getInstance();
        results.addAll(index.getAllKeys(project));
        return ArrayUtil.toStringArray(results);
    }

    @NotNull
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        MQL4ClassNameIndex index = MQL4ClassNameIndex.getInstance();
        Collection<MQL4ClassElement> elements = index.get(name, project, GlobalSearchScope.allScope(project));
        return elements.toArray(new NavigationItem[elements.size()]);
    }
}
