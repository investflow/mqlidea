package ru.investflow.mql.psi;

import com.intellij.psi.tree.IElementType;

public interface MQL4LexerElements {
    /**
     * Token type for a sequence of whitespace characters.
     * Better have it the same with default -> in this case it is the same in during parsing and in built Psi elements tree.
     */
    IElementType WHITE_SPACE = com.intellij.psi.TokenType.WHITE_SPACE;

    /**
     * Token type for a character which is not valid in the position where it was encountered,
     * according to the language grammar.
     */
    IElementType BAD_CHARACTER = new MQL4ElementType("BAD_CHARACTER");


    IElementType IDENTIFIER = new MQL4ElementType("IDENTIFIER");

    // Comments
    IElementType LINE_COMMENT = new MQL4ElementType("LINE_COMMENT");
    IElementType BLOCK_COMMENT = new MQL4ElementType("BLOCK_COMMENT");

    // Line terminators
    IElementType LINE_TERMINATOR = new MQL4ElementType("LINE_TERMINATOR");

    // Escape character
    IElementType ESCAPE = new MQL4ElementType("ESCAPE");

    // Literals
    IElementType CHAR_LITERAL = new MQL4ElementType("CHAR_LITERAL");
    IElementType DOUBLE_LITERAL = new MQL4ElementType("DOUBLE_LITERAL");
    IElementType INCLUDE_STRING_LITERAL = new MQL4ElementType("INCLUDE_STRING_LITERAL");
    IElementType INTEGER_LITERAL = new MQL4ElementType("INTEGER_LITERAL");
    IElementType STRING_LITERAL = new MQL4ElementType("STRING_LITERAL");
    IElementType COLOR_CONSTANT_LITERAL = new MQL4ElementType("COLOR_CONSTANT_LITERAL");
    IElementType COLOR_STRING_LITERAL = new MQL4ElementType("COLOR_STRING_LITERAL");

    // Braces and separators
    IElementType COLON_COLON = new MQL4ElementType("COLON_COLON");
    IElementType COLON = new MQL4ElementType("COLON");
    IElementType COMMA = new MQL4ElementType("COMMA");
    IElementType DOT = new MQL4ElementType("DOT");
    IElementType GT = new MQL4ElementType("GT");
    IElementType LT = new MQL4ElementType("LT");
    IElementType L_CURLY_BRACKET = new MQL4ElementType("L_CURLY_BRACKET");
    IElementType R_CURLY_BRACKET = new MQL4ElementType("R_CURLY_BRACKET");
    IElementType L_ROUND_BRACKET = new MQL4ElementType("L_ROUND_BRACKET");
    IElementType R_ROUND_BRACKET = new MQL4ElementType("R_ROUND_BRACKET");
    IElementType L_SQUARE_BRACKET = new MQL4ElementType("L_SQUARE_BRACKET");
    IElementType R_SQUARE_BRACKET = new MQL4ElementType("R_SQUARE_BRACKET");
    IElementType SEMICOLON = new MQL4ElementType("SEMICOLON");

    // Preprocessor  macros
    IElementType DEFINE_PP_KEYWORD = new MQL4ElementType("DEFINE_PP_KEYWORD");
    IElementType ENDIF_PP_KEYWORD = new MQL4ElementType("ENDIF_PP_KEYWORD");
    IElementType ELSE_PP_KEYWORD = new MQL4ElementType("ELSE_PP_KEYWORD");
    IElementType IFDEF_PP_KEYWORD = new MQL4ElementType("IFDEF_PP_KEYWORD");
    IElementType IFNDEF_PP_KEYWORD = new MQL4ElementType("IFNDEF_PP_KEYWORD");
    IElementType IMPORT_PP_KEYWORD = new MQL4ElementType("IMPORT_PP_KEYWORD");
    IElementType INCLUDE_PP_KEYWORD = new MQL4ElementType("INCLUDE_PP_KEYWORD");
    IElementType PROPERTY_PP_KEYWORD = new MQL4ElementType("PROPERTY_PP_KEYWORD");
    IElementType RESOURCE_PP_KEYWORD = new MQL4ElementType("RESOURCE_PP_KEYWORD");
    IElementType UNDEF_PP_KEYWORD = new MQL4ElementType("UNDEF_PP_KEYWORD");

    // Data types
    IElementType BOOL_KEYWORD = new MQL4ElementType("BOOL_KEYWORD");
    IElementType CHAR_KEYWORD = new MQL4ElementType("CHAR_KEYWORD");
    IElementType CLASS_KEYWORD = new MQL4ElementType("CLASS_KEYWORD");
    IElementType COLOR_KEYWORD = new MQL4ElementType("COLOR_KEYWORD");
    IElementType DATETIME_KEYWORD = new MQL4ElementType("DATETIME_KEYWORD");
    IElementType DOUBLE_KEYWORD = new MQL4ElementType("DOUBLE_KEYWORD");
    IElementType ENUM_KEYWORD = new MQL4ElementType("ENUM_KEYWORD");
    IElementType FLOAT_KEYWORD = new MQL4ElementType("FLOAT_KEYWORD");
    IElementType INT_KEYWORD = new MQL4ElementType("INT_KEYWORD");
    IElementType INTERFACE_KEYWORD = new MQL4ElementType("INTERFACE_KEYWORD");
    IElementType LONG_KEYWORD = new MQL4ElementType("LONG_KEYWORD");
    IElementType RETURN_KEYWORD = new MQL4ElementType("RETURN_KEYWORD");
    IElementType SHORT_KEYWORD = new MQL4ElementType("SHORT_KEYWORD");
    IElementType STRING_KEYWORD = new MQL4ElementType("STRING_KEYWORD");
    IElementType STRUCT_KEYWORD = new MQL4ElementType("STRUCT_KEYWORD");
    IElementType TEMPLATE_KEYWORD = new MQL4ElementType("TEMPLATE_KEYWORD");
    IElementType TYPENAME_KEYWORD = new MQL4ElementType("TYPENAME_KEYWORD");
    IElementType UCHAR_KEYWORD = new MQL4ElementType("UCHAR_KEYWORD");
    IElementType UINT_KEYWORD = new MQL4ElementType("UINT_KEYWORD");
    IElementType ULONG_KEYWORD = new MQL4ElementType("ULONG_KEYWORD");
    IElementType USHORT_KEYWORD = new MQL4ElementType("USHORT_KEYWORD");
    IElementType VOID_KEYWORD = new MQL4ElementType("VOID_KEYWORD");

    // Memory classes
    IElementType EXTERN_KEYWORD = new MQL4ElementType("EXTERN_KEYWORD");
    IElementType INPUT_KEYWORD = new MQL4ElementType("INPUT_KEYWORD");
    IElementType STATIC_KEYWORD = new MQL4ElementType("STATIC_KEYWORD");

    // Access specifiers
    IElementType CONST_KEYWORD = new MQL4ElementType("CONST_KEYWORD");
    IElementType PRIVATE_KEYWORD = new MQL4ElementType("PRIVATE_KEYWORD");
    IElementType PROTECTED_KEYWORD = new MQL4ElementType("PROTECTED_KEYWORD");
    IElementType PUBLIC_KEYWORD = new MQL4ElementType("PUBLIC_KEYWORD");
    IElementType VIRTUAL_KEYWORD = new MQL4ElementType("VIRTUAL_KEYWORD");

    // Operators
    IElementType AND = new MQL4ElementType("AND");
    IElementType AND_EQ = new MQL4ElementType("AND_EQ");
    IElementType BOOL_AND = new MQL4ElementType("BOOL_AND");
    IElementType BOOL_OR = new MQL4ElementType("BOOL_OR");
    IElementType DIV = new MQL4ElementType("DIV");
    IElementType DIV_EQ = new MQL4ElementType("DIV_EQ");
    IElementType EQ = new MQL4ElementType("EQ");
    IElementType EQ_EQ = new MQL4ElementType("EQ_EQ");
    IElementType GT_EQ = new MQL4ElementType("GT_EQ");
    IElementType LESS_EQ = new MQL4ElementType("LESS_EQ");
    IElementType MINUS = new MQL4ElementType("MINUS");
    IElementType MINUS_EQ = new MQL4ElementType("MINUS_EQ");
    IElementType MINUS_MINUS = new MQL4ElementType("MINUS_MINUS");
    IElementType MOD = new MQL4ElementType("MOD");
    IElementType MOD_EQ = new MQL4ElementType("MOD_EQ");
    IElementType MUL = new MQL4ElementType("MUL");
    IElementType MUL_EQ = new MQL4ElementType("MUL_EQ");
    IElementType NOT = new MQL4ElementType("NOT");
    IElementType NOT_EQ = new MQL4ElementType("NOT_EQ");
    IElementType OR = new MQL4ElementType("OR");
    IElementType OR_EQ = new MQL4ElementType("OR_EQ");
    IElementType PLUS = new MQL4ElementType("PLUS");
    IElementType PLUS_EQ = new MQL4ElementType("PLUS_EQ");
    IElementType PLUS_PLUS = new MQL4ElementType("PLUS_PLUS");
    IElementType POW = new MQL4ElementType("POW");
    IElementType POW_EQ = new MQL4ElementType("POW_EQ");
    IElementType QUESTION = new MQL4ElementType("QUESTION");
    IElementType SH_LEFT = new MQL4ElementType("SH_LEFT");
    IElementType SH_LEFT_EQ = new MQL4ElementType("SH_LEFT_EQ");
    IElementType SH_RIGHT = new MQL4ElementType("SH_RIGHT");
    IElementType SH_RIGHT_EQ = new MQL4ElementType("SH_RIGHT_EQ");
    IElementType TILDA = new MQL4ElementType("TILDA");
    IElementType TILDA_EQ = new MQL4ElementType("TILDA_EQ");
    IElementType USH_RIGHT = new MQL4ElementType("USH_RIGHT");
    IElementType USH_RIGHT_EQ = new MQL4ElementType("USH_RIGHT_EQ");
    IElementType XOR = new MQL4ElementType("XOR");
    IElementType XOR_EQ = new MQL4ElementType("XOR_EQ");

    // Keywords
    IElementType BREAK_KEYWORD = new MQL4ElementType("BREAK_KEYWORD");
    IElementType CASE_KEYWORD = new MQL4ElementType("CASE_KEYWORD");
    IElementType CONTINUE_KEYWORD = new MQL4ElementType("CONTINUE_KEYWORD");
    IElementType DEFAULT_KEYWORD = new MQL4ElementType("DEFAULT_KEYWORD");
    IElementType DELETE_KEYWORD = new MQL4ElementType("DELETE_KEYWORD");
    IElementType DO_KEYWORD = new MQL4ElementType("DO_KEYWORD");
    IElementType ELSE_KEYWORD = new MQL4ElementType("ELSE_KEYWORD");
    IElementType FALSE_KEYWORD = new MQL4ElementType("FALSE_KEYWORD");
    IElementType FOR_KEYWORD = new MQL4ElementType("FOR_KEYWORD");
    IElementType IF_KEYWORD = new MQL4ElementType("IF_KEYWORD");
    IElementType NEW_KEYWORD = new MQL4ElementType("NEW_KEYWORD");
    IElementType OPERATOR_KEYWORD = new MQL4ElementType("OPERATOR_KEYWORD");
    IElementType SIZEOF_KEYWORD = new MQL4ElementType("SIZEOF_KEYWORD");
    IElementType SWITCH_KEYWORD = new MQL4ElementType("SWITCH_KEYWORD");
    IElementType THIS_KEYWORD = new MQL4ElementType("THIS_KEYWORD");
    IElementType TRUE_KEYWORD = new MQL4ElementType("TRUE_KEYWORD");
    IElementType WHILE_KEYWORD = new MQL4ElementType("WHILE_KEYWORD");
}
