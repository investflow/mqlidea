package ru.investflow.mql.psi;

import com.intellij.psi.tree.TokenSet;

public class MQL4TokenTypeSets implements MQL4TokenTypes {

    public static final TokenSet COMMENTS = TokenSet.create(
            LINE_COMMENT,
            BLOCK_COMMENT
    );


    public static final TokenSet STRINGS_AND_CHARS = TokenSet.create(
            LIT_STRING, LIT_CHAR
    );

    public static final TokenSet NUMBERS = TokenSet.create(
            LIT_INTEGER, LIT_FLOAT
    );

    public static final TokenSet OPERATORS = TokenSet.create(
            OP_EQ
    );

    public static final TokenSet KEYWORDS = TokenSet.create(
            KW_BOOL, KW_CHAR, KW_CLASS, KW_COLOR, KW_DATETIME, KW_DOUBLE,
            KW_ENUM, KW_FLOAT, KW_INT, KW_LONG, KW_SHORT, KW_STRING,
            KW_STRUCT, KW_UCHAR, KW_UINT, KW_ULONG, KW_USHORT, KW_VOID,
            KW_CONST, KW_PUBLIC, KW_PRIVATE, KW_VIRTUAL, KW_PROTECTED,
            KW_EXTERN, KW_INPUT, KW_STATIC,
            KW_BREAK, KW_CASE, KW_CONTINUE, KW_DEFAULT, KW_DELETE,
            KW_DO, KW_ELSE, KW_FOR, KW_IF, KW_NEW,
            KW_OPERATOR, KW_RETURN, KW_SIZEOF, KW_SWITCH, KW_WHILE,
            KW_FALSE, KW_THIS, KW_TRUE, KW_STRICT
    );

}
