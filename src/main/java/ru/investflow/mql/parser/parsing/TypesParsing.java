package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;

public class TypesParsing implements MQL4Elements {


    public static boolean parseUserDataType(@NotNull PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseCustomTypeName")) {
            return false;
        }
        if (b.getTokenType() != IDENTIFIER) {
            return false;
        }
        b.advanceLexer(); // name

        int templateParamSize = tryParseTemplateList(b, 0);
        if (templateParamSize > 0) {
            PsiBuilder.Marker m = b.mark();
            try {
                ParsingUtils.advanceLexer(b, templateParamSize);
            } finally {
                m.done(TYPE_TEMPLATE_BLOCK);
            }
        }
        return true;
    }


    /**
     * Tries to parse template args list from 'offset'.
     * Returns -1 if parsing fail. Otherwise return offset of the template last char.
     * Does not affect parsing state.
     */
    public static int tryParseTemplateList(@NotNull PsiBuilder b, int offset) {
        return tryParseTemplateList(b, offset, new TParsingState());
    }

    private static int tryParseTemplateList(@NotNull PsiBuilder b, int offset, @NotNull TParsingState state) {
        if (state.depth > 10) {
            return -1;
        }
        int pos = offset;
        if (b.lookAhead(pos) != LT) {
            return -1;
        }
        state.depth++;
        pos++; // '<'
        while (true) {
            if (b.lookAhead(pos) == GT) {
                pos++; // '>'
                state.depth--;
                return pos - offset;
            }
            if (b.lookAhead(pos) == SH_RIGHT) {
                pos++;
                state.depth -= 2;
                return state.depth < 0 ? -1 : pos - offset;
            }
            IElementType typeName = b.lookAhead(pos);
            if (typeName != IDENTIFIER && !MQL4TokenSets.DATA_TYPES.contains(typeName)) {
                return -1;
            }
            pos++; // identifier or raw type
            if (b.lookAhead(pos) == MUL) {
                pos++; // pointer type
            }
            if (b.lookAhead(pos) == LT) { // sub-template
                int dPos = tryParseTemplateList(b, pos, state);
                if (dPos <= 0) {
                    return -1;
                }
                pos += dPos;
                if (state.depth == 0) {
                    return pos - offset;
                }
                if (b.lookAhead(pos) == MUL) {
                    pos++; // pointer type
                }
            }
            if (b.lookAhead(pos) == COMMA) {
                pos++; // ','
            }
        }
    }

    public static class TParsingState {
        public int depth = 0;
    }
}
