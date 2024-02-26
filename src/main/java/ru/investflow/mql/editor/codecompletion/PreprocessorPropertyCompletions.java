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
import ru.investflow.mql.doc.MQL4DocumentationProvider;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4PreprocessorPropertyBlock;

import java.util.ArrayList;
import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static ru.investflow.mql.editor.codecompletion.MQL4CompletionContributor.mql4;
import static ru.investflow.mql.inspection.PreprocessorPropertyInspection.VALIDATORS_BY_NAME;

/**
 * Code completion for preprocessor #property block.
 */
public class PreprocessorPropertyCompletions {

    static final PsiElementPattern.Capture<PsiElement> IN_PROPERTIES_BLOCK_P1 = mql4().inside(MQL4PreprocessorPropertyBlock.class).afterLeaf(psiElement(MQL4Elements.PROPERTY_PP_KEYWORD));
    static final PsiElementPattern.Capture<PsiElement> IN_PROPERTIES_BLOCK_COLOR1 = mql4().inside(MQL4PreprocessorPropertyBlock.class).afterLeaf("indicator_color1", "indicator_color2", "indicator_color3", "indicator_color4", "indicator_color5", "indicator_color6", "indicator_color7", "indicator_color8", "indicator_color9");
    static final PsiElementPattern.Capture<PsiElement> IN_PROPERTIES_BLOCK_COLOR2 = mql4().inside(MQL4PreprocessorPropertyBlock.class).afterLeaf("indicator_levelcolor");

    /**
     * Adds to completion all #property keys.
     */
    static class MQL4PropertiesBlockKeyCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
            VALIDATORS_BY_NAME.keySet().forEach(k -> result.addElement(LookupElementBuilder.create(k)));
        }
    }

    /**
     * Adds to completion all color constants
     */
    static class MQL4PropertiesBlockColorCompletionProvider extends CompletionProvider<CompletionParameters> {
        private static final List<LookupElementBuilder> COLOR_COMPLETIONS = new ArrayList<>();

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
            if (COLOR_COMPLETIONS.isEmpty()) {
                MQL4DocumentationProvider.getEntries().stream().filter(e -> e.text.startsWith("clr")).map(e -> LookupElementBuilder.create(e.text)).forEach(COLOR_COMPLETIONS::add);
            }
            COLOR_COMPLETIONS.forEach(result::addElement);
        }
    }

    public static PsiElementPattern.Capture<PsiElement> extend(MQL4CompletionContributor contributor, PsiElementPattern.Capture<PsiElement> filter) {
        contributor.extend(CompletionType.BASIC, IN_PROPERTIES_BLOCK_P1, new MQL4PropertiesBlockKeyCompletionProvider());
        contributor.extend(CompletionType.BASIC, IN_PROPERTIES_BLOCK_COLOR1, new MQL4PropertiesBlockColorCompletionProvider());
        contributor.extend(CompletionType.BASIC, IN_PROPERTIES_BLOCK_COLOR2, new MQL4PropertiesBlockColorCompletionProvider());

        return filter
                .andNot(IN_PROPERTIES_BLOCK_P1)
                .andNot(IN_PROPERTIES_BLOCK_COLOR1)
                .andNot(IN_PROPERTIES_BLOCK_COLOR2);
    }

}
