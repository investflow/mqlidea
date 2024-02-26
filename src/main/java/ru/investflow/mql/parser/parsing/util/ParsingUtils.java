package ru.investflow.mql.parser.parsing.util;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;

import java.util.List;

import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.error;

public class ParsingUtils implements MQL4Elements {

    public static void repeat(int n, Runnable r) {
        for (int i = 0; i < n; i++) {
            r.run();
        }
    }

    public static IElementType advanceLexer(@NotNull PsiBuilder b, int n) {
        repeat(n, b::advanceLexer);
        return b.getTokenType();
    }

    public static IElementType advanceLexer(@NotNull PsiBuilder b) {
        b.advanceLexer();
        return b.getTokenType();
    }

    //TODO: optimize this method: pass from & to idx
    public static boolean containsEndOfLine(@Nullable String text) {
        return text != null && text.contains("\n");
    }


    /**
     * @return true if there is new line between startPos and currentPos or currentPos is EOF.
     */
    public static boolean containsEndOfLineOrFile(@NotNull PsiBuilder b, int startPos) {
        return containsEndOfLine(b, startPos) || b.getOriginalText().length() == b.getCurrentOffset();
    }

    /**
     * @return true if there is new line between startPos and currentPos.
     */
    private static boolean containsEndOfLine(@NotNull PsiBuilder b, int startPos) {
        String text = b.getOriginalText().subSequence(startPos, b.getCurrentOffset()).toString();
        return containsEndOfLine(text);
    }

    /**
     * Safe to use with any kind of tokens that are hidden by PSI-Builder by default (like whitespaces)
     *
     * @return returns true if stopToken was found.
     */
    public static boolean advanceLexerUntil(@NotNull PsiBuilder b, @NotNull TokenSet stopTypes, TokenAdvanceMode advanceStopTokens) {
        if (advanceStopTokens == TokenAdvanceMode.ADVANCE) {
            return advanceLexerUntil(b, stopTypes, TokenSet.EMPTY);
        } else {
            return advanceLexerUntil(b, TokenSet.EMPTY, stopTypes);
        }
    }

    /**
     * Safe to use with any kind of tokens that are hidden by PSI-Builder by default (like whitespaces)
     *
     * @return returns true if stopToken was found.
     */
    public static boolean advanceLexerUntil(@NotNull PsiBuilder b, @NotNull TokenSet stopTypesAdvance, TokenSet stopTypesDoNotAdvance) {
        b.setTokenTypeRemapper((t, start, end, text) -> stopTypesAdvance.contains(t) || stopTypesDoNotAdvance.contains(t) ? ParsingMarker.forType(t) : t);
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
                if (stopTypesDoNotAdvance.contains(m.originalToken)) {
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

    public static int matchSequenceN(@NotNull PsiBuilder b, @NotNull List<PatternMatcher> matchers, int nAhead) {
        int a = nAhead;
        for (PatternMatcher m : matchers) {
            int da = m.match(b, a);
            if (da < 0) {
                return -1;
            }
            a += da;
        }
        return a - nAhead;
    }

    public static boolean checkTokenOrFail(@NotNull PsiBuilder b, @NotNull IElementType type) {
        return parseTokenOrFail(b, type, false);
    }

    public static boolean parseTokenOrFail(@NotNull PsiBuilder b, @NotNull IElementType type) {
        return parseTokenOrFail(b, type, true);
    }

    public static boolean parseTokenOrFail(@NotNull PsiBuilder b, @NotNull IElementType type, boolean advanceOnSuccess) {
        if (b.getTokenType() == type) {
            if (advanceOnSuccess) {
                b.advanceLexer();
            }
            return true;
        }
        String error = "Expected: " + type;
        if (type == IDENTIFIER) {
            error = "Identifier expected";
        } else if (type == L_ROUND_BRACKET) {
            error = "Left brace expected";
        } else if (type == R_ROUND_BRACKET) {
            error = "Right brace expected";
        } else if (type == SEMICOLON) {
            error = "Semicolon expected";
        }
        error(b, error);
        return false;
    }

    public static boolean nextTokenIs(@NotNull PsiBuilder b, int l, @NotNull String recursionGuard, @NotNull IElementType type) {
        return recursion_guard_(b, l, recursionGuard) && b.getTokenType() == type;
    }

    public static boolean nextTokenIn(@NotNull PsiBuilder b, int l, @NotNull String recursionGuard, @NotNull TokenSet set) {
        return recursion_guard_(b, l, recursionGuard) && set.contains(b.getTokenType());
    }

    public static boolean parseType(@NotNull PsiBuilder b, @NotNull TokenSet typeSet) {
        if (typeSet.contains(b.getTokenType())) {
            b.advanceLexer();
            return true;
        }
        return false;
    }

    public static boolean hasElementInRange(PsiBuilder b, int range, IElementType e) {
        for (int i = 0; i < range; i++) {
            IElementType t = b.lookAhead(i);
            if (t == null) {
                break;
            }
            if (t == e) {
                return true;
            }
        }
        return false;
    }

    public static int countLookAhead(@NotNull PsiBuilder b, int offset, @NotNull TokenSet tokens) {
        int res = 0;
        while (true) {
            IElementType t = b.lookAhead(offset + res);
            if (t == null || !tokens.contains(t)) {
                break;
            }
            res++;
        }
        return res;
    }
}
