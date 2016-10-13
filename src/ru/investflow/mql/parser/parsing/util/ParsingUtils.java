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

    /**
     * Safe to use with any kind of tokens that are hidden by PSI-Builder by default (like whitespaces)
     *
     * @return returns true if stopToken was found.
     */
    public static boolean advanceLexerUntil(@NotNull PsiBuilder b, @NotNull TokenSet stopTypes, TokenAdvanceMode advanceMode) {
        b.setTokenTypeRemapper((source, start, end, text) -> stopTypes.contains(source) ? new ParsingMarker(source) : source);
        try {
            // find the token
            while (!(b.getTokenType() instanceof ParsingMarker)) {
                b.advanceLexer();
                if (b.eof()) {
                    return false;
                }
            }
            // restore original token, remove marker and advance if needed.
            do {
                ParsingMarker m = (ParsingMarker) b.getTokenType();
                b.remapCurrentToken(m.originalToken);
                if (advanceMode == TokenAdvanceMode.DO_NOT_ADVANCE) {
                    break;
                }
                b.advanceLexer();
            } while (!b.eof() && b.getTokenType() instanceof ParsingMarker);
            return true;
        } finally {
            b.setTokenTypeRemapper(null);
        }
    }

    /**
     * Safe to use with any kind of tokens that are hidden by PSI-Builder by default (like whitespaces)
     *
     * @return returns true if stopToken was found.
     */
    public static boolean advanceLexerUntil(@NotNull PsiBuilder b, @NotNull IElementType type, @NotNull TokenAdvanceMode mode) {
        return advanceLexerUntil(b, TokenSet.create(type), mode);
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
