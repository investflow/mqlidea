package ru.investflow.mql.psi;

import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.stub.MQL4StubElements;

public interface MQL4Elements extends MQL4StubElements, MQL4LexerElements {

    // Composite elements: blocks, statements, lists
    IElementType PREPROCESSOR_DEFINE_BLOCK = new MQL4ElementType("PREPROCESSOR_DEFINE_BLOCK");
    IElementType PREPROCESSOR_IMPORT_BLOCK = new MQL4ElementType("PREPROCESSOR_IMPORT_BLOCK");
    IElementType PREPROCESSOR_INCLUDE_BLOCK = new MQL4ElementType("PREPROCESSOR_INCLUDE_BLOCK");
    IElementType PREPROCESSOR_PROPERTY_BLOCK = new MQL4ElementType("PREPROCESSOR_PROPERTY_BLOCK");
    IElementType PREPROCESSOR_UNDEF_BLOCK = new MQL4ElementType("PREPROCESSOR_UNDEF_BLOCK");

    IElementType FUNCTION_DECLARATION = new MQL4ElementType("FUNCTION_DECLARATION");
    IElementType FUNCTION_DEFINITION = new MQL4ElementType("FUNCTION_DEFINITION");
    IElementType FUNCTION_ARG = new MQL4ElementType("FUNCTION_ARG");
    IElementType FUNCTION_ARGS_LIST = new MQL4ElementType("FUNCTION_ARGS_LIST");

    IElementType ENUM_STATEMENT = new MQL4ElementType("ENUM_STATEMENT");
    IElementType ENUM_FIELDS_LIST = new MQL4ElementType("ENUM_FIELDS_LIST");
    IElementType ENUM_FIELD = new MQL4ElementType("ENUM_FIELD");

    IElementType CLASS_INHERITANCE_LIST = new MQL4ElementType("CLASS_INHERITANCE_LIST");
    IElementType CLASS_INHERITANCE_ITEM = new MQL4ElementType("CLASS_INHERITANCE_ITEM");
    IElementType CLASS_INNER_BLOCK = new MQL4ElementType("CLASS_INNER_BLOCK");
    IElementType CLASS_INIT_BLOCK = new MQL4ElementType("CLASS_INIT_BLOCK");
    IElementType TYPE_TEMPLATE_BLOCK = new MQL4ElementType("TYPE_TEMPLATE_BLOCK");

    IElementType BRACKETS_BLOCK = new MQL4ElementType("BRACKETS_BLOCK");
    IElementType CAST_BLOCK = new MQL4ElementType("CAST_BLOCK");

    IElementType EMPTY_STATEMENT = new MQL4ElementType("EMPTY_STATEMENT");
    IElementType SINGLE_WORD_STATEMENT = new MQL4ElementType("SINGLE_WORD_STATEMENT");

    IElementType VAR_DECLARATION_STATEMENT = new MQL4ElementType("VAR_DECLARATION_STATEMENT");
    IElementType VAR_DEFINITION_LIST = new MQL4ElementType("VAR_DEFINITION_LIST");
    IElementType VAR_DEFINITION = new MQL4ElementType("VAR_DEFINITION");

    // Special element types for syntax highlighter only
    IElementType SYNTAX_BUILT_IN_CONSTANT = new MQL4ElementType("SYNTAX_BUILT_IN_CONSTANT");
    IElementType SYNTAX_BUILT_IN_FUNCTION = new MQL4ElementType("SYNTAX_BUILT_IN_FUNCTION");
}
