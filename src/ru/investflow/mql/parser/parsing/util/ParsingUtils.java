package ru.investflow.mql.parser.parsing.util;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.ITokenTypeRemapper;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.sun.istack.internal.Nullable;
import ru.investflow.mql.psi.MQL4TokenTypes;

import static ru.investflow.mql.parser.parsing.util.ParsingUtils.NewLineAdvanceMode.DEFAULT;
import static ru.investflow.mql.psi.MQL4TokenTypes.LINE_TERMINATOR;
import static ru.investflow.mql.psi.MQL4TokenTypes.RECOVERY_LINE_TERMINATOR;

public class ParsingUtils {
    public static final ITokenTypeRemapper NEW_LINE_REMAPPER = (source, start, end, text) -> source == LINE_TERMINATOR ? RECOVERY_LINE_TERMINATOR : source;

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
                boolean foundNewLine = token == MQL4TokenTypes.RECOVERY_LINE_TERMINATOR;
                if (foundNewLine) { // restore original new line token, remove artificial one.
                    b.remapCurrentToken(MQL4TokenTypes.LINE_TERMINATOR);
                    return MQL4TokenTypes.LINE_TERMINATOR;
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
