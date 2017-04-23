package ru.investflow.mql.parser.parsing.util;

import com.intellij.lang.PsiBuilder;

public interface PatternMatcher {
    /**
     * < 0 not matched. >=0, number of tokens to skip
     */
    int match(PsiBuilder b, int ahead);
}
