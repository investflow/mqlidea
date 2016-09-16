package ru.investflow.mql.parser.parsing.util;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Predicate;
import com.sun.istack.internal.Nullable;
import ru.investflow.mql.psi.MQL4Tokens;

public class ParsingUtils implements MQL4Tokens {

    public static boolean containsEndOfLine(@Nullable String text) {
        return text != null && text.contains("\n");
    }

    public static boolean containsEndOfLine(@NotNull PsiBuilder b, int startPos) {
        String text = b.getOriginalText().subSequence(startPos, b.getCurrentOffset()).toString();
        return containsEndOfLine(text);
    }

    @Nullable
    public static IElementType advanceLexerUntil(@NotNull PsiBuilder b, @NotNull IElementType type) {
        b.setTokenTypeRemapper((source, start, end, text) -> source == type ? PARSING_MARKER : source);
        IElementType token = null;
        try {
            while (!b.eof()) {
                token = b.getTokenType();
                boolean found = token == PARSING_MARKER;
                if (found) { // restore original token, remove artificial one.
                    b.remapCurrentToken(type);
                    return LINE_TERMINATOR;
                }
                b.advanceLexer();
            }
        } finally {
            b.setTokenTypeRemapper(null);
        }
        return token;
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
