package ru.investflow.mql.parser.parsing.util;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;

import java.util.List;

import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;

public class ParsingUtils implements MQL4Elements {

    public static TokenSet STATEMENT_TERMINATORS = TokenSet.create(SEMICOLON, R_CURLY_BRACKET, R_ROUND_BRACKET);

    public static void repeat(int n, Runnable r) {
        for (int i = 1; i <= n; i++) {
            r.run();
        }
    }

    public static void advanceLexer(PsiBuilder b, int n) {
        repeat(n, b::advanceLexer);
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
        b.setTokenTypeRemapper((source, start, end, text) -> stopTypes.contains(source) ? ParsingMarker.forType(source) : source);
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
                if (advanceStopTokens == TokenAdvanceMode.DO_NOT_ADVANCE) {
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

    public static boolean matchSequence(@NotNull PsiBuilder b, @NotNull List<PatternMatcher> matchers, int nAhead) {
        return matchSequenceN(b, matchers, nAhead) >= 0;
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
        b.error(error);
        return false;
    }

    public static boolean parseKeywordOrFail(@NotNull PsiBuilder b, @NotNull IElementType type) {
        if (b.getTokenType() == type) {
            b.advanceLexer();
            return true;
        }
        b.error("'" + type.toString().replace("_KEYWORD", "").toLowerCase() + "' expected");
        return false;
    }

    public static boolean nextTokenIs(@NotNull PsiBuilder b, int l, @NotNull String recursionGuard, @NotNull IElementType type) {
        return recursion_guard_(b, l, recursionGuard) && b.getTokenType() == type;
    }

    public static boolean nextTokenIn(@NotNull PsiBuilder b, int l, @NotNull String recursionGuard, @NotNull TokenSet set) {
        return recursion_guard_(b, l, recursionGuard) && set.contains(b.getTokenType());
    }

    /**
     * Advances current token and sets error message on it.
     */
    public static void advanceWithError(@NotNull PsiBuilder b, @NotNull String message) {
        PsiBuilder.Marker errorBlock = b.mark();
        b.advanceLexer();
        errorBlock.error(message);
    }
}
