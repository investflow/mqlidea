package ru.investflow.mql.parser.parsing.util;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ParsingMarker extends IElementType {

    private static final Map<IElementType, ParsingMarker> MARKERS_CACHE = new HashMap<>();

    @NotNull
    public final IElementType originalToken;

    public ParsingMarker(@NotNull IElementType originalToken) {
        super("ParsingMarker:" + originalToken, originalToken.getLanguage());
        this.originalToken = originalToken;
    }

    public static IElementType forType(@NotNull IElementType source) {
        return MARKERS_CACHE.computeIfAbsent(source, ParsingMarker::new);
    }
}
