package ru.investflow.mql.parser.parsing.statement;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import ru.investflow.mql.parser.parsing.ExpressionParsing;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;

import static ru.investflow.mql.parser.parsing.util.ParsingErrors.error;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;

public class EnumParsing implements MQL4Elements {

    public static final TokenSet ON_ERROR_ENUM_ADVANCE_TOKENS = TokenSet.create(R_CURLY_BRACKET);
    public static final TokenSet ON_ERROR_ENUM_DO_NOT_ADVANCE_TOKENS = TokenSet.create(SEMICOLON);

    /**
     * Form: enum [TYPE] { v1, v2=1, v3=SOME_CONST }
     */
    public static boolean parseEnum(PsiBuilder b, int l) {
        if (b.getTokenType() != ENUM_KEYWORD) {
            return false;
        }
        Marker m = b.mark();
        try {
            b.advanceLexer(); // 'enum'
            if (b.getTokenType() == IDENTIFIER) {
                b.advanceLexer(); // type
            }
            if (b.getTokenType() != L_CURLY_BRACKET) {
                error(b, "Enum block is expected");
                return false;
            }
            b.advanceLexer(); // '{'
            if (!parseEnumFields(b, l)) {
                advanceLexerUntil(b, ON_ERROR_ENUM_ADVANCE_TOKENS, ON_ERROR_ENUM_DO_NOT_ADVANCE_TOKENS);
                return true;
            }
            b.advanceLexer(); // '}'
        } finally {
            m.done(ENUM_STATEMENT);
        }
        return true;
    }

    /**
     * Form: name [=[IDENTIFIER | CONSTANT]]
     */
    private static boolean parseEnumFields(PsiBuilder b, int l) {
        Marker fieldList = b.mark();
        try {
            while (b.getTokenType() != R_CURLY_BRACKET && !b.eof()) {
                Marker field = b.mark();
                try {
                    //  === First element ===
                    IElementType t1 = b.getTokenType();
                    if (t1 != IDENTIFIER) {  // field name
                        error(b, ParsingErrors.UNEXPECTED_TOKEN);
                        return false;
                    }
                    b.advanceLexer(); // field name

                    // === End of element or '=' ===
                    IElementType t2 = b.getTokenType();
                    if (t2 == R_CURLY_BRACKET) {
                        break;
                    }
                    if (t2 == COMMA) {
                        b.advanceLexer(); // ','
                        continue;
                    } else if (t2 == EQ) {
                        b.advanceLexer(); // '='
                    } else {
                        return false;
                    }

                    // === Value ===
                    boolean v = ExpressionParsing.parseCompileTimeEvalExpression(b, l, false, ExpressionParsing.COMPILE_TIME_NUMBER, R_CURLY_BRACKET);
                    if (!v) {
                        return false;
                    }
                    IElementType t4 = b.getTokenType();
                    if (t4 == R_CURLY_BRACKET) {
                        break;
                    }
                    if (t4 == COMMA) {
                        b.advanceLexer(); // ','
                        continue;
                    }
                    error(b, ParsingErrors.UNEXPECTED_TOKEN);
                    return false;
                } finally {
                    field.done(ENUM_FIELD);
                }
            }
            return !b.eof();
        } finally {
            fieldList.done(ENUM_FIELDS_LIST);
        }
    }


}
