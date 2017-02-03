package ru.investflow.mql.editor.codecompletion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4File;

import java.util.stream.Stream;

import static ru.investflow.mql.editor.codecompletion.MQL4CompletionContributor.mql4;

/**
 * Code completion for all preprocessor keywords.
 */
public class PreprocessorCompletions {

    static final PsiElementPattern.Capture<PsiElement> STARTING_PROPERTIES_BLOCK = mql4().withParent(MQL4File.class).afterLeaf("#");

    /**
     * Adds to completion all preprocessor keywords.
     */
    static class MQL4PreprocessorKeywordsCompletion extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            Stream.of("define", "undef", "import", "include", "property")
                    .forEach(k -> result.addElement(LookupElementBuilder.create(k)));
        }
    }

    public static PsiElementPattern.Capture<PsiElement> extend(MQL4CompletionContributor contributor, PsiElementPattern.Capture<PsiElement> filter) {
        contributor.extend(CompletionType.BASIC, STARTING_PROPERTIES_BLOCK, new MQL4PreprocessorKeywordsCompletion());
        return filter.andNot(STARTING_PROPERTIES_BLOCK);
    }

}
