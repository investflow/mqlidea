package ru.investflow.mql.editor.codecompletion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.util.ProcessingContext;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Language;
import ru.investflow.mql.doc.DocEntry;
import ru.investflow.mql.doc.MQL4DocumentationProvider;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Code completion for MQL4 files.
 */
public class MQL4CompletionContributor extends CompletionContributor {

    public MQL4CompletionContributor() {
        PsiElementPattern.Capture<PsiElement> filter = mql4();

        filter = CommentsCompletions.extend(this, filter);
        filter = PreprocessorCompletions.extend(this, filter);
        filter = PreprocessorPropertyCompletions.extend(this, filter);
        filter = PreprocessorIncludeCompletions.extend(this, filter);

        extend(CompletionType.BASIC, filter, new MQL4KeywordCompletionProvider());
        extend(CompletionType.BASIC, filter, new MQL4IdentifiersCompletionProvider());
    }

    /**
     * Cached keyword completions
     */
    private static final List<LookupElementBuilder> KEYWORD_COMPLETIONS = new ArrayList<>();

    private static List<LookupElementBuilder> getKeywordCompletions() {
        if (KEYWORD_COMPLETIONS.isEmpty()) {
            for (DocEntry e : MQL4DocumentationProvider.getEntries()) {
                KEYWORD_COMPLETIONS.add(LookupElementBuilder.create(e.text));
            }
        }
        return KEYWORD_COMPLETIONS;
    }

    /**
     * Adds to completion all MQL4 keywords, built-in constants and function names.
     */
    private static class MQL4KeywordCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
            getKeywordCompletions().forEach(result::addElement);
        }
    }

    /**
     * Adds to completion all named identifiers from file.
     */
    public static class MQL4IdentifiersCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
            parameters.getOriginalFile().accept(new PsiRecursiveElementVisitor() {
                @Override
                public void visitElement(@NotNull PsiElement element) {
                    super.visitElement(element);
                    if (element.getNode().getElementType() == MQL4Elements.IDENTIFIER) {
                        result.addElement(LookupElementBuilder.create(element.getText()));
                    }
                }
            });
        }
    }

    /**
     * Helper method for all filters.
     */
    static PsiElementPattern.Capture<PsiElement> mql4() {
        return psiElement().withLanguage(MQL4Language.INSTANCE);
    }

}
