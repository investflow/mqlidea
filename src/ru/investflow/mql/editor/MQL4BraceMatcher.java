package ru.investflow.mql.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;

/**
 * Brace matcher for MQL4.
 */
public class MQL4BraceMatcher implements PairedBraceMatcher {

    private static BracePair[] PAIRS = {
            new BracePair(MQL4Elements.L_CURLY_BRACKET, MQL4Elements.R_CURLY_BRACKET, true),
            new BracePair(MQL4Elements.L_ROUND_BRACKET, MQL4Elements.R_ROUND_BRACKET, true),
            new BracePair(MQL4Elements.L_SQUARE_BRACKET, MQL4Elements.R_SQUARE_BRACKET, true)
    };

    @Override
    public BracePair[] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType L_CURLY_BRACKETType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }

}
