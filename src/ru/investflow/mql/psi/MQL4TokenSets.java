package ru.investflow.mql.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MQL4TokenSets implements MQL4Elements {

    public static final TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT);
    public static final TokenSet COMMENTS_OR_WS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT, WHITE_SPACE, LINE_TERMINATOR);

    public static final TokenSet PREPROCESSOR = TokenSet.create(
            DEFINE_KEYWORD,
            ENDIF_KEYWORD,
            IFDEF_KEYWORD,
            IMPORT_KEYWORD,
            INCLUDE_KEYWORD,
            PROPERTY_KEYWORD,
            RESOURCE_KEYWORD,
            UNDEF_KEYWORD
    );

    public static final TokenSet LITERALS = TokenSet.create(STRING_LITERAL, CHAR_LITERAL, INTEGER_LITERAL, DOUBLE_LITERAL,
            FALSE_KEYWORD, TRUE_KEYWORD, COLOR_CONSTANT_LITERAL, COLOR_STRING_LITERAL);

    public static final TokenSet STRINGS_AND_CHARS = TokenSet.create(STRING_LITERAL, CHAR_LITERAL, INCLUDE_STRING_LITERAL);

    public static final TokenSet NUMBERS = TokenSet.create(INTEGER_LITERAL, DOUBLE_LITERAL);

    public static final TokenSet LEFT_BRACKETS = TokenSet.create(L_CURLY_BRACKET, L_ROUND_BRACKET, L_SQUARE_BRACKET);
    public static final TokenSet RIGHT_BRACKETS = TokenSet.create(R_CURLY_BRACKET, R_ROUND_BRACKET, R_SQUARE_BRACKET);
    public static final TokenSet BRACKETS = TokenSet.orSet(LEFT_BRACKETS, RIGHT_BRACKETS);

    public static final TokenSet OPERATORS = TokenSet.create(
            EQ, EQ_EQ, PLUS_EQ, MINUS_EQ, MUL_EQ, DIV_EQ, MOD_EQ, AND_EQ, OR_EQ, XOR_EQ, TILDA_EQ, SH_LEFT_EQ, SH_RIGHT_EQ, USH_RIGHT_EQ,
            POW_EQ, PLUS_PLUS, MINUS_MINUS, BOOL_OR, BOOL_AND, USH_RIGHT, SH_RIGHT, SH_LEFT, POW, LESS_EQ, GT_EQ, NOT_EQ, OR, XOR, PLUS, MINUS, MUL,
            TILDA, DIV, MOD, AND, NOT, QUESTION
    );

    public static final TokenSet DATA_TYPES = TokenSet.create(
            BOOL_KEYWORD,
            CHAR_KEYWORD,
            CLASS_KEYWORD,
            COLOR_KEYWORD,
            DATETIME_KEYWORD,
            DOUBLE_KEYWORD,
            ENUM_KEYWORD,
            FLOAT_KEYWORD,
            INT_KEYWORD,
            INTERFACE_KEYWORD,
            LONG_KEYWORD,
            SHORT_KEYWORD,
            STRING_KEYWORD,
            STRUCT_KEYWORD,
            UCHAR_KEYWORD,
            UINT_KEYWORD,
            ULONG_KEYWORD,
            USHORT_KEYWORD,
            VOID_KEYWORD
    );

    public static final TokenSet VALID_IMPORT_DATA_TYPES = TokenSet.create(
            BOOL_KEYWORD,
            CHAR_KEYWORD,
            DOUBLE_KEYWORD,
            FLOAT_KEYWORD,
            INT_KEYWORD,
            LONG_KEYWORD,
            SHORT_KEYWORD,
            STRING_KEYWORD,
            UCHAR_KEYWORD,
            UINT_KEYWORD,
            ULONG_KEYWORD,
            USHORT_KEYWORD,
            VOID_KEYWORD
    );

    public static final TokenSet CLASS_STRUCT_INTERFACE = TokenSet.create(CLASS_KEYWORD, STRUCT_KEYWORD, INTERFACE_KEYWORD);

    @NotNull
    public static IElementType getLeftBracketFor(@NotNull IElementType rightBracket) {
        IElementType result = rightBracket == R_CURLY_BRACKET ? L_CURLY_BRACKET
                : rightBracket == R_ROUND_BRACKET ? L_ROUND_BRACKET
                : rightBracket == R_SQUARE_BRACKET ? L_SQUARE_BRACKET : null;
        return Objects.requireNonNull(result, "Illegal open bracket: " + rightBracket);
    }

    @NotNull
    public static IElementType getRightBracketFor(@NotNull IElementType leftBracket) {
        IElementType result = leftBracket == L_CURLY_BRACKET ? R_CURLY_BRACKET
                : leftBracket == L_ROUND_BRACKET ? R_ROUND_BRACKET
                : leftBracket == L_SQUARE_BRACKET ? R_SQUARE_BRACKET : null;
        return Objects.requireNonNull(result, "Illegal open bracket: " + leftBracket);
    }
}
