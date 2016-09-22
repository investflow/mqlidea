package ru.investflow.mql.parser.parsing.util;

import org.jetbrains.annotations.NotNull;

import com.intellij.psi.tree.IElementType;

public class ParsingMarker extends IElementType {
    @NotNull
    public final IElementType originalToken;

    public ParsingMarker(@NotNull IElementType originalToken) {
        super("ParsingMarker:" + originalToken, originalToken.getLanguage());
        this.originalToken = originalToken;
    }

}
