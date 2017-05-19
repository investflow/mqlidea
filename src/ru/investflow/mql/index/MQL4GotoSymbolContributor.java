package ru.investflow.mql.index;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.HashSet;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4ClassElement;
import ru.investflow.mql.psi.impl.MQL4FunctionElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MQL4GotoSymbolContributor implements ChooseByNameContributor {

    @NotNull
    public String[] getNames(Project project, boolean includeNonProjectItems) {
        Set<String> results = new HashSet<>();

        Collection<String> allClasses = MQL4ClassNameIndex.getInstance().getAllKeys(project);
        results.addAll(allClasses);

        Collection<String> allFunctions = MQL4FunctionNameIndex.getInstance().getAllKeys(project);
        results.addAll(allFunctions);

        return ArrayUtil.toStringArray(results);
    }

    @NotNull
    public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        List<NavigationItem> result = new ArrayList<>();

        Collection<MQL4ClassElement> classes = MQL4ClassNameIndex.getInstance().get(name, project, scope);
        result.addAll(classes);

        Collection<MQL4FunctionElement> functions = MQL4FunctionNameIndex.getInstance().get(name, project, scope);
        result.addAll(functions);

        return result.toArray(new NavigationItem[result.size()]);
    }
}
