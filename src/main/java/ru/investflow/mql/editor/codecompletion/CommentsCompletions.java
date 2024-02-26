package ru.investflow.mql.editor.codecompletion;

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import ru.investflow.mql.editor.codecompletion.MQL4CompletionContributor.MQL4IdentifiersCompletionProvider;

import static ru.investflow.mql.editor.codecompletion.MQL4CompletionContributor.mql4;


public class CommentsCompletions {
    static final PsiElementPattern.Capture<PsiElement> IN_COMMENT = mql4().inside(PsiComment.class);

    public static PsiElementPattern.Capture<PsiElement> extend(MQL4CompletionContributor contributor, PsiElementPattern.Capture<PsiElement> filter) {
        contributor.extend(CompletionType.BASIC, CommentsCompletions.IN_COMMENT, new MQL4IdentifiersCompletionProvider());
        return filter.andNot(CommentsCompletions.IN_COMMENT);
    }
}
