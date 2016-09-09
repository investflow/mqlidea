package ru.investflow.mql.parser.parsing.util;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sun.istack.internal.Nullable;
import ru.investflow.mql.psi.MQL4Tokens;

import static ru.investflow.mql.parser.parsing.util.ParsingUtils.NewLineAdvanceMode.DEFAULT;

public class ParsingUtils implements MQL4Tokens {
    public static final ITokenTypeRemapper NEW_LINE_REMAPPER = (source, start, end, text) -> source == LINE_TERMINATOR ? RECOVERY_LINE_TERMINATOR : source;

    public static boolean containsEndOfLine(@Nullable String text) {
        return text != null && text.contains("\n");
    }

    public enum NewLineAdvanceMode {
        DEFAULT,
        STOP_IF_FIRST_TOKEN_IS_NOT_NEW_LINE
    }

    public static void advanceLexerUntilNewLine(@NotNull PsiBuilder b) {
        tryAdvanceLexerUntilNewLine(b, DEFAULT);
    }

    @Nullable
    public static IElementType tryAdvanceLexerUntilNewLine(@NotNull PsiBuilder b, NewLineAdvanceMode mode) {
        b.setTokenTypeRemapper(NEW_LINE_REMAPPER);
        IElementType token = null;
        try {
            while (!b.eof()) {
                token = b.getTokenType();
                boolean foundNewLine = token == RECOVERY_LINE_TERMINATOR;
                if (foundNewLine) { // restore original new line token, remove artificial one.
                    b.remapCurrentToken(LINE_TERMINATOR);
                    return LINE_TERMINATOR;
                }
                if (mode == NewLineAdvanceMode.STOP_IF_FIRST_TOKEN_IS_NOT_NEW_LINE) {
                    return token;
                }
                b.advanceLexer();
            }
        } finally {
            b.setTokenTypeRemapper(null);
        }
        return token;
    }

}
