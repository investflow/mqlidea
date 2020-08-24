package ru.investflow.mql.editor.codecompletion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.impl.MQL4PreprocessorIncludeBlock;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.investflow.mql.editor.codecompletion.MQL4CompletionContributor.mql4;

/**
 * Code completion for preprocessor #property block.
 */
public class PreprocessorIncludeCompletions {

    private static final Pattern INCLUDE_FILES_PATTERN = Pattern.compile(".*\\.mqh");

    static final PsiElementPattern.Capture<PsiElement> IN_INCLUDE_BLOCK_P1 = mql4().inside(MQL4PreprocessorIncludeBlock.class);

    /**
     * Adds to completion all color constants
     */
    static class PreprocessorIncludeCompletionProvider extends CompletionProvider<CompletionParameters> {

        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
            VirtualFile vFile = parameters.getOriginalFile().getVirtualFile();
            String path = vFile == null ? null : vFile.getCanonicalPath();
            if (path == null) {
                return;
            }
            File dir = new File(path).getParentFile();
            if (!dir.exists()) {
                return;
            }
            // first impl is for 'proof of concept' only and is very simple:
            List<File> localFiles = FileUtil.findFilesByMask(INCLUDE_FILES_PATTERN, dir);
            String filteredPrefix = dir.getAbsolutePath() + "/";

            PsiElement pos = parameters.getPosition();
            PsiElement prevElement = pos.getPrevSibling();
            boolean hasQuote = prevElement != null && prevElement.getText().equals("\"");
            boolean rightAfterInclude = !hasQuote && pos.getParent().getPrevSibling().getNode().getElementType() == MQL4Elements.INCLUDE_PP_KEYWORD;
            String completionPrefix = (rightAfterInclude ? " " : "") + (hasQuote ? "" : "\"");
            List<String> completions = localFiles.stream()
                    .filter(f -> !f.getAbsolutePath().equals(path)) // filter out current file
                    .map(File::getAbsolutePath)
                    .map(p -> completionPrefix + p.substring(filteredPrefix.length()) + "\"") // full name completion
                    .collect(Collectors.toList());

            completions.forEach(c -> result.addElement(LookupElementBuilder.create(c)));
        }
    }

    public static PsiElementPattern.Capture<PsiElement> extend(MQL4CompletionContributor contributor, PsiElementPattern.Capture<PsiElement> filter) {
        contributor.extend(CompletionType.BASIC, IN_INCLUDE_BLOCK_P1, new PreprocessorIncludeCompletionProvider());
        return filter.andNot(IN_INCLUDE_BLOCK_P1);
    }

}
