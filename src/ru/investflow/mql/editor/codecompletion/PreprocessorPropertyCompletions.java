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
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4PreprocessorPropertyBlock;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static ru.investflow.mql.editor.codecompletion.MQL4CompletionContributor.mql4;
import static ru.investflow.mql.inspection.PreprocessorPropertyInspection.VALIDATORS_BY_NAME;

/**
 * Code completion for preprocessor #property block.
 */
public class PreprocessorPropertyCompletions {

    static final PsiElementPattern.Capture<PsiElement> IN_PROPERTIES_BLOCK_P1 = mql4().inside(MQL4PreprocessorPropertyBlock.class).afterLeaf(psiElement(MQL4Elements.PROPERTY_KEYWORD));

    /**
     * Adds to completion all named identifiers from file.
     */
    static class MQL4PropertiesBlockCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
            VALIDATORS_BY_NAME.keySet().forEach(k -> result.addElement(LookupElementBuilder.create(k)));
        }
    }

    public static PsiElementPattern.Capture<PsiElement> extend(MQL4CompletionContributor contributor, PsiElementPattern.Capture<PsiElement> filter) {
        contributor.extend(CompletionType.BASIC, IN_PROPERTIES_BLOCK_P1, new MQL4PropertiesBlockCompletionProvider());
        return filter.andNot(IN_PROPERTIES_BLOCK_P1);
    }

}
