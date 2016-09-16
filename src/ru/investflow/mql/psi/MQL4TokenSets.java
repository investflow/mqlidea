package ru.investflow.mql.psi;

import com.intellij.psi.tree.TokenSet;

public class MQL4TokenSets implements MQL4Tokens {

    public static final TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT);

    public static final TokenSet PREPROCESSOR = TokenSet.create(
            DEFINE_KEYWORD,
            IMPORT_KEYWORD,
            INCLUDE_KEYWORD,
            PROPERTY_KEYWORD,
            UNDEF_KEYWORD
    );

    public static final TokenSet LITERALS = TokenSet.create(STRING_LITERAL, CHAR_LITERAL, INTEGER_LITERAL, DOUBLE_LITERAL, INCLUDE_STRING_LITERAL);

    public static final TokenSet STRINGS_AND_CHARS = TokenSet.create(STRING_LITERAL, CHAR_LITERAL, INCLUDE_STRING_LITERAL);

    public static final TokenSet NUMBERS = TokenSet.create(INTEGER_LITERAL, DOUBLE_LITERAL);

    public static final TokenSet OPERATORS = TokenSet.create(
//            OP_EQ
    );

    public static final TokenSet KEYWORDS = TokenSet.create(
            //TODO:
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

}
