package ru.investflow.mql.psi;

import com.intellij.psi.tree.TokenSet;

public class MQL4TokenSets implements MQL4Tokens {

    public static final TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT);

    public static final TokenSet LITERALS = TokenSet.create(STRING_LITERAL, CHAR_LITERAL, INTEGER_LITERAL, DOUBLE_LITERAL);

    public static final TokenSet STRINGS_AND_CHARS = TokenSet.create(STRING_LITERAL, CHAR_LITERAL);

    public static final TokenSet NUMBERS = TokenSet.create(INTEGER_LITERAL, DOUBLE_LITERAL);

    public static final TokenSet OPERATORS = TokenSet.create(
//            OP_EQ
    );

    public static final TokenSet KEYWORDS = TokenSet.create(
            //TODO:
    );

}
