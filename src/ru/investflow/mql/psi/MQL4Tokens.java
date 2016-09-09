package ru.investflow.mql.psi;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

public interface MQL4Tokens extends TokenType {

    IElementType IDENTIFIER = new MQL4ElementType("IDENTIFIER");

    // Comments
    IElementType LINE_COMMENT = new MQL4ElementType("LINE_COMMENT");
    IElementType BLOCK_COMMENT = new MQL4ElementType("BLOCK_COMMENT");

    // Line terminators
    IElementType LINE_TERMINATOR = new MQL4ElementType("LINE_TERMINATOR");
    IElementType RECOVERY_LINE_TERMINATOR = new MQL4ElementType("RECOVERY_LINE_TERMINATOR");

    // Literals
    IElementType INTEGER_LITERAL = new MQL4ElementType("INTEGER_LITERAL");
    IElementType DOUBLE_LITERAL = new MQL4ElementType("DOUBLE_LITERAL");
    IElementType STRING_LITERAL = new MQL4ElementType("STRING_LITERAL");
    IElementType CHAR_LITERAL = new MQL4ElementType("CHAR_LITERAL");

    // Braces and separators
    IElementType LPARENTH = new MQL4ElementType("LPARENTH");
    IElementType RPARENTH = new MQL4ElementType("RPARENTH");
    IElementType LBRACE = new MQL4ElementType("LBRACE");
    IElementType RBRACE = new MQL4ElementType("RBRACE");
    IElementType LBRACKET = new MQL4ElementType("LBRACKET");
    IElementType RBRACKET = new MQL4ElementType("RBRACKET");
    IElementType SEMICOLON = new MQL4ElementType("SEMICOLON");
    IElementType COMMA = new MQL4ElementType("COMMA");
    IElementType DOT = new MQL4ElementType("DOT");

    // Preprocessor
    IElementType PROPERTY_KEYWORD = new MQL4ElementType("PROPERTY_KEYWORD");
    IElementType DEFINE_KEYWORD = new MQL4ElementType("DEFINE_KEYWORD");
    IElementType UNDEF_KEYWORD = new MQL4ElementType("UNDEF_KEYWORD");
}
