package ru.investflow.mql.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;

public class MQL4BraceMatcher implements PairedBraceMatcher {

    private static BracePair[] PAIRS = {
            new BracePair(MQL4Elements.LBRACE, MQL4Elements.RBRACE, true),
            new BracePair(MQL4Elements.LPARENTH, MQL4Elements.RPARENTH, true),
            new BracePair(MQL4Elements.LBRACKET, MQL4Elements.RBRACKET, true)
    };

    @Override
    public BracePair[] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }

}
