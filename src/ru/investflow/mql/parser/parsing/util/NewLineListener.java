package ru.investflow.mql.parser.parsing.util;

import com.intellij.lang.WhitespaceSkippedCallback;
import com.intellij.psi.tree.IElementType;

import static ru.investflow.mql.psi.MQL4TokenTypes.LINE_TERMINATOR;

public class NewLineListener implements WhitespaceSkippedCallback {

    public boolean newLineFound;

    @Override
    public void onSkip(IElementType type, int start, int end) {
        newLineFound = newLineFound || type == LINE_TERMINATOR;
    }
}
