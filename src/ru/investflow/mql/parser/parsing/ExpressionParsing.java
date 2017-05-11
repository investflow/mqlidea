package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;

public class ExpressionParsing implements MQL4Elements {

    public static final TokenSet COMPILE_TIME_NUMBER = TokenSet.create(IDENTIFIER, INTEGER_LITERAL, CHAR_LITERAL, COLOR_CONSTANT_LITERAL, COLOR_STRING_LITERAL, DOUBLE_LITERAL);
    public static final TokenSet COMPILE_TIME_VALUE = TokenSet.orSet(COMPILE_TIME_NUMBER, TokenSet.create(STRING_LITERAL, TRUE_KEYWORD, FALSE_KEYWORD));

    public static final TokenSet SIZEOF_TYPES = TokenSet.create(
            BOOL_KEYWORD,
            CHAR_KEYWORD,
            COLOR_KEYWORD,
            DATETIME_KEYWORD,
            DOUBLE_KEYWORD,
            FLOAT_KEYWORD,
            INT_KEYWORD,
            LONG_KEYWORD,
            SHORT_KEYWORD,
            STRING_KEYWORD,
            UCHAR_KEYWORD,
            UINT_KEYWORD,
            ULONG_KEYWORD,
            ULONG_KEYWORD);

    public static boolean parseSizeOf(@NotNull PsiBuilder b, int l) {
        if (b.getTokenType() != MQL4Elements.SIZEOF_KEYWORD) {
            return false;
        }
        b.advanceLexer(); // 'sizeof'
        if (b.getTokenType() != L_ROUND_BRACKET) {
            b.error(ParsingErrors.UNEXPECTED_TOKEN);
            return true;
        }
        PsiBuilder.Marker brackets = b.mark();
        try {
            b.advanceLexer();// '('
            if (SIZEOF_TYPES.contains(b.getTokenType())) {
                b.advanceLexer(); // type
            } else {
                if (!parseCompileTimeEvalExpression(b, l + 1, false, COMPILE_TIME_VALUE, R_ROUND_BRACKET)) {
                    return true;
                }
            }
            if (b.getTokenType() != R_ROUND_BRACKET) {
                b.error(ParsingErrors.NO_MATCHING_CLOSING_BRACKET);
                return true;
            }
            b.advanceLexer(); // ')'
        } finally {
            brackets.done(BRACKETS_BLOCK);
        }
        return true;
    }

    private static final TokenSet OPERATORS = TokenSet.create(PLUS, MINUS, MUL, DIV, SH_LEFT, SH_RIGHT, AND, OR, XOR);

    public static boolean parseOperator(@NotNull PsiBuilder b) {
        if (!OPERATORS.contains(b.getTokenType())) {
            return false;
        }
        b.advanceLexer();
        return true;
    }


    public static final TokenSet ON_ERROR_PARSE_EXPRESSION_DO_NOT_ADVANCE_TOKENS = TokenSet.create(SEMICOLON, R_CURLY_BRACKET, COMMA);
    public static final TokenSet SIGN = TokenSet.create(PLUS, MINUS);


    public static boolean parseCompileTimeEvalExpression(PsiBuilder b, int l, boolean nested, @NotNull TokenSet valueTypes, IElementType closingBracket) {
        if (!recursion_guard_(b, l, "parseCompileTimeEvalExpression")) {
            return false;
        }
        while (!b.eof()) {
            boolean validPrefixOp = ParsingUtils.parseType(b, SIGN)
                    || parseCast(b);
            if (validPrefixOp) {
                continue;
            }
            boolean validValue =
                    parseCompileTimeEvalExpressionInBraces(b, l, nested, valueTypes, closingBracket)
                            || ParsingUtils.parseType(b, valueTypes)
                            || parseSizeOf(b, l);
            if (!validValue) {
                return false;
            }
            IElementType t = b.getTokenType();
            if (t == COMMA || (!nested && t == closingBracket) || (nested && t == R_ROUND_BRACKET)) {
                return true;
            }
            if (!ExpressionParsing.parseOperator(b)) {
                b.error(ParsingErrors.UNEXPECTED_TOKEN);
                return false;
            }
        }
        return false;
    }

    private static boolean parseCompileTimeEvalExpressionInBraces(PsiBuilder b, int l, boolean nested, @NotNull TokenSet valueTypes, IElementType closingBracket) {
        if (b.getTokenType() != L_ROUND_BRACKET) {
            return false;
        }
        PsiBuilder.Marker bracketBlock = b.mark();
        try {
            b.advanceLexer(); // '('
            if (!parseCompileTimeEvalExpression(b, l + 1, true, valueTypes, closingBracket)) {
                return false;
            }
            if (b.getTokenType() != R_ROUND_BRACKET) {
                b.error(ParsingErrors.NO_MATCHING_CLOSING_BRACKET);
                advanceLexerUntil(b, TokenSet.EMPTY, ON_ERROR_PARSE_EXPRESSION_DO_NOT_ADVANCE_TOKENS);
                return false;
            }
            b.advanceLexer(); // ')'
            IElementType t = b.getTokenType();
            if (t == COMMA || (!nested && t == R_CURLY_BRACKET) || (nested && t == R_ROUND_BRACKET)) {
                return true;
            }
        } finally {
            bracketBlock.done(MQL4Elements.BRACKETS_BLOCK);
        }
        return true;
    }

    private static boolean parseCast(@NotNull PsiBuilder b) {
        if (b.lookAhead(0) == L_ROUND_BRACKET && MQL4TokenSets.DATA_TYPES.contains(b.lookAhead(1)) && b.lookAhead(2) == R_ROUND_BRACKET) {
            PsiBuilder.Marker castBlock = b.mark();
            ParsingUtils.advanceLexer(b, 3);
            castBlock.done(CAST_BLOCK);
            return true;
        }
        return false;
    }
}
