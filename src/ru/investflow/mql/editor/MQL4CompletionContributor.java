package ru.investflow.mql.editor;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.doc.DocEntry;
import ru.investflow.mql.doc.MQL4DocumentationProvider;

import java.util.ArrayList;
import java.util.List;

public class MQL4CompletionContributor extends CompletionContributor {

    private static final List<LookupElementBuilder> KEYWORD_COMPLETIONS = new ArrayList<>();

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (KEYWORD_COMPLETIONS.isEmpty()) {
            for (DocEntry e : MQL4DocumentationProvider.getEntries()) {
                KEYWORD_COMPLETIONS.add(LookupElementBuilder.create(e.text));
            }
        }
        for (LookupElementBuilder b : KEYWORD_COMPLETIONS) {
            result.addElement(b);
        }
    }
}
