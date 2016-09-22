package ru.investflow.mql.parser.parsing.util;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.Predicate;
import ru.investflow.mql.psi.MQL4Tokens;

public class ParsingUtils implements MQL4Tokens {

    public static boolean containsEndOfLine(@Nullable String text) {
        return text != null && text.contains("\n");
    }

    public static boolean containsEndOfLine(@NotNull PsiBuilder b, int startPos) {
        String text = b.getOriginalText().subSequence(startPos, b.getCurrentOffset()).toString();
        return containsEndOfLine(text);
    }

    public enum StopTokenAdvanceMode {
        ADVANCE_STOP_TOKENS,
        STOP_AND_RETURN_STOP_TOKEN
    }

    /**
     * TODO: document me!
     */
    @Nullable
    public static IElementType advanceLexerUntil(@NotNull PsiBuilder b, @NotNull TokenSet stopTypes, StopTokenAdvanceMode stopMode) {
        b.setTokenTypeRemapper((source, start, end, text) -> stopTypes.contains(source) ? new ParsingMarker(source) : source);
        IElementType token = null;
        boolean advancingStops = false;
        try {
            while (!b.eof()) {
                token = b.getTokenType();
                if (token instanceof ParsingMarker) { // restore original token, remove artificial one.
                    ParsingMarker m = (ParsingMarker) token;
                    b.remapCurrentToken(m.originalToken);
                    if (stopMode == StopTokenAdvanceMode.STOP_AND_RETURN_STOP_TOKEN) {
                        return m.originalToken;
                    }
                    advancingStops = true;
                } else if (advancingStops) {
                    return null;
                }
                b.advanceLexer();
            }
        } finally {
            b.setTokenTypeRemapper(null);
        }
        return token;
    }

    @Nullable
    public static IElementType advanceLexerUntil(@NotNull PsiBuilder b, @NotNull IElementType type) {
        return advanceLexerUntil(b, TokenSet.create(type), StopTokenAdvanceMode.STOP_AND_RETURN_STOP_TOKEN);
    }

    @SuppressWarnings("unchecked")
    public static boolean matchSequence(@NotNull PsiBuilder b, @NotNull List<Predicate<IElementType>> predicates) {
        for (int i = 0; i < predicates.size(); i++) {
            Predicate<IElementType> p = predicates.get(i);
            IElementType t = b.lookAhead(i);
            if (!p.apply(t)) {
                return false;
            }
        }
        return true;
    }
}
