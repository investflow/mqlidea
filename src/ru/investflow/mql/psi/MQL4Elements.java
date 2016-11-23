package ru.investflow.mql.psi;

import com.intellij.psi.tree.IElementType;

public interface MQL4Elements {

    /**
     * Token type for a sequence of whitespace characters.
     */
    IElementType WHITE_SPACE = new MQL4ElementType("WHITE_SPACE");

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

    // Literals
    IElementType CHAR_LITERAL = new MQL4ElementType("CHAR_LITERAL");
    IElementType DOUBLE_LITERAL = new MQL4ElementType("DOUBLE_LITERAL");
    IElementType INTEGER_LITERAL = new MQL4ElementType("INTEGER_LITERAL");
    IElementType STRING_LITERAL = new MQL4ElementType("STRING_LITERAL");
    IElementType INCLUDE_STRING_LITERAL = new MQL4ElementType("INCLUDE_STRING_LITERAL");

    // Braces and separators
    IElementType COMMA = new MQL4ElementType("COMMA");
    IElementType DOT = new MQL4ElementType("DOT");
    IElementType LBRACE = new MQL4ElementType("LBRACE");
    IElementType LBRACKET = new MQL4ElementType("LBRACKET");
    IElementType LPARENTH = new MQL4ElementType("LPARENTH");
    IElementType RBRACE = new MQL4ElementType("RBRACE");
    IElementType RBRACKET = new MQL4ElementType("RBRACKET");
    IElementType RPARENTH = new MQL4ElementType("RPARENTH");
    IElementType COLON = new MQL4ElementType("COLON");
    IElementType SEMICOLON = new MQL4ElementType("SEMICOLON");
    IElementType LT = new MQL4ElementType("LT");
    IElementType GT = new MQL4ElementType("GT");

    // Preprocessor
    IElementType DEFINE_KEYWORD = new MQL4ElementType("DEFINE_KEYWORD");
    IElementType IMPORT_KEYWORD = new MQL4ElementType("IMPORT_KEYWORD");
    IElementType INCLUDE_KEYWORD = new MQL4ElementType("INCLUDE_KEYWORD");
    IElementType PROPERTY_KEYWORD = new MQL4ElementType("PROPERTY_KEYWORD");
    IElementType UNDEF_KEYWORD = new MQL4ElementType("UNDEF_KEYWORD");

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
    IElementType LONG_KEYWORD = new MQL4ElementType("LONG_KEYWORD");
    IElementType SHORT_KEYWORD = new MQL4ElementType("SHORT_KEYWORD");
    IElementType STRING_KEYWORD = new MQL4ElementType("STRING_KEYWORD");
    IElementType STRUCT_KEYWORD = new MQL4ElementType("STRUCT_KEYWORD");
    IElementType UCHAR_KEYWORD = new MQL4ElementType("UCHAR_KEYWORD");
    IElementType UINT_KEYWORD = new MQL4ElementType("UINT_KEYWORD");
    IElementType ULONG_KEYWORD = new MQL4ElementType("ULONG_KEYWORD");
    IElementType USHORT_KEYWORD = new MQL4ElementType("USHORT_KEYWORD");
    IElementType VOID_KEYWORD = new MQL4ElementType("VOID_KEYWORD");

    // Memory classes
    IElementType EXTERN_KEYWORD = new MQL4ElementType("EXTERN_KEYWORD");
    IElementType INPUT_KEYWORD = new MQL4ElementType("INPUT_KEYWORD");
    IElementType STATIC_KEYWORD = new MQL4ElementType("STATIC_KEYWORD");

    // Access specificators
    IElementType CONST_KEYWORD = new MQL4ElementType("CONST_KEYWORD");


    // Operators
    IElementType EQ = new MQL4ElementType("EQ");
    IElementType EQ_EQ = new MQL4ElementType("EQ_EQ");
    IElementType PLUS_EQ = new MQL4ElementType("PLUS_EQ");
    IElementType MINUS_EQ = new MQL4ElementType("MINUS_EQ");
    IElementType MUL_EQ = new MQL4ElementType("MUL_EQ");
    IElementType DIV_EQ = new MQL4ElementType("DIV_EQ");
    IElementType MOD_EQ = new MQL4ElementType("MOD_EQ");
    IElementType AND_EQ = new MQL4ElementType("AND_EQ");
    IElementType OR_EQ = new MQL4ElementType("OR_EQ");
    IElementType XOR_EQ = new MQL4ElementType("XOR_EQ");
    IElementType TILDA_EQ = new MQL4ElementType("TILDA_EQ");
    IElementType SH_LEFT_EQ = new MQL4ElementType("SH_LEFT_EQ");
    IElementType SH_RIGHT_EQ = new MQL4ElementType("SH_RIGHT_EQ");
    IElementType USH_RIGHT_EQ = new MQL4ElementType("USH_RIGHT_EQ");
    IElementType POW_EQ = new MQL4ElementType("POW_EQ");
    IElementType PLUS_PLUS = new MQL4ElementType("PLUS_PLUS");
    IElementType MINUS_MINUS = new MQL4ElementType("MINUS_MINUS");
    IElementType BOOL_OR = new MQL4ElementType("BOOL_OR");
    IElementType BOOL_AND = new MQL4ElementType("BOOL_AND");
    IElementType USH_RIGHT = new MQL4ElementType("USH_RIGHT");
    IElementType SH_RIGHT = new MQL4ElementType("SH_RIGHT");
    IElementType SH_LEFT = new MQL4ElementType("SH_LEFT");
    IElementType POW = new MQL4ElementType("POW");
    IElementType LESS_EQ = new MQL4ElementType("LESS_EQ");
    IElementType GT_EQ = new MQL4ElementType("GT_EQ");
    IElementType NOT_EQ = new MQL4ElementType("NOT_EQ");
    IElementType OR = new MQL4ElementType("OR");
    IElementType XOR = new MQL4ElementType("XOR");
    IElementType PLUS = new MQL4ElementType("PLUS");
    IElementType MINUS = new MQL4ElementType("MINUS");
    IElementType MUL = new MQL4ElementType("MUL");
    IElementType TILDA = new MQL4ElementType("TILDA");
    IElementType DIV = new MQL4ElementType("DIV");
    IElementType MOD = new MQL4ElementType("MOD");
    IElementType AND = new MQL4ElementType("AND");
    IElementType NOT = new MQL4ElementType("NOT");
    IElementType QUESTION = new MQL4ElementType("QUESTION");

    // Keywords
    IElementType BREAK_KEYWORD = new MQL4ElementType("BREAK_KEYWORD");
    IElementType CONTINUE_KEYWORD = new MQL4ElementType("CONTINUE_KEYWORD");
    IElementType FOR_KEYWORD = new MQL4ElementType("FOR_KEYWORD");
    IElementType DO_KEYWORD = new MQL4ElementType("DO_KEYWORD");
    IElementType WHILE_KEYWORD = new MQL4ElementType("WHILE_KEYWORD");
    IElementType IF_KEYWORD = new MQL4ElementType("IF_KEYWORD");
    IElementType ELSE_KEYWORD = new MQL4ElementType("ELSE_KEYWORD");
    IElementType SWITCH_KEYWORD = new MQL4ElementType("SWITCH_KEYWORD");
    IElementType CASE_KEYWORD = new MQL4ElementType("CASE_KEYWORD");
    IElementType DEFAULT_KEYWORD = new MQL4ElementType("DEFAULT_KEYWORD");
    IElementType FALSE_KEYWORD = new MQL4ElementType("FALSE_KEYWORD");
    IElementType TRUE_KEYWORD = new MQL4ElementType("TRUE_KEYWORD");
    IElementType SIZEOF_KEYWORD = new MQL4ElementType("TRUE_KEYWORD");


    /**
     * Composite elements: blocks, statements, lists
     */

    IElementType PREPROCESSOR_BLOCK = new MQL4ElementType("PREPROCESSOR_BLOCK");
    IElementType PREPROCESSOR_DEFINE_BLOCK = new MQL4ElementType("PREPROCESSOR_DEFINE_BLOCK");
    IElementType PREPROCESSOR_IMPORT_BLOCK = new MQL4ElementType("PREPROCESSOR_IMPORT_BLOCK");
    IElementType PREPROCESSOR_INCLUDE_BLOCK = new MQL4ElementType("PREPROCESSOR_INCLUDE_BLOCK");
    IElementType PREPROCESSOR_PROPERTY_BLOCK = new MQL4ElementType("PREPROCESSOR_PROPERTY_BLOCK");
    IElementType PREPROCESSOR_UNDEF_BLOCK = new MQL4ElementType("PREPROCESSOR_UNDEF_BLOCK");

    IElementType FUNCTION_DECLARATION = new MQL4ElementType("FUNCTION_DECLARATION");
    IElementType FUNCTION_DEFINITION = new MQL4ElementType("FUNCTION_DEFINITION");
    IElementType FUNCTION_PARAMETERS_LIST = new MQL4ElementType("FUNCTION_PARAMETERS_LIST");
    IElementType FUNCTION_PARAMETER = new MQL4ElementType("FUNCTION_PARAMETER");

    IElementType CODE_BLOCK = new MQL4ElementType("CODE_BLOCK");

    IElementType EMPTY_STATEMENT = new MQL4ElementType("EMPTY_STATEMENT");
    IElementType SINGLE_WORD_STATEMENT = new MQL4ElementType("SINGLE_WORD_STATEMENT");

    IElementType VAR_DECLARATION_STATEMENT = new MQL4ElementType("VAR_DECLARATION_STATEMENT");
    IElementType VAR_DEFINITION_LIST = new MQL4ElementType("VAR_DEFINITION_LIST");
    IElementType VAR_ASSIGNMENT_LIST = new MQL4ElementType("VAR_ASSIGNMENT_LIST");
    IElementType VAR_DEFINITION = new MQL4ElementType("VAR_DEFINITION");

    IElementType FOR_LOOP = new MQL4ElementType("FOR_LOOP");
    IElementType DO_LOOP = new MQL4ElementType("WHILE_LOOP");
    IElementType WHILE_LOOP = new MQL4ElementType("WHILE_LOOP");
    IElementType FOR_LOOP_SECTION_1 = new MQL4ElementType("FOR_LOOP_SECTION_1");

    IElementType IF_ELSE_BLOCK = new MQL4ElementType("IF_ELSE_BLOCK");

    IElementType SWITCH_BLOCK = new MQL4ElementType("SWITCH_BLOCK");
    IElementType SWITCH_CASE_BLOCK = new MQL4ElementType("SWITCH_CASE_BLOCK");

}
