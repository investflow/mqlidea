package ru.investflow.mql.psi;

import com.intellij.psi.tree.IElementType;

public interface MQL4Elements {

    IElementType PREPROCESSOR_BLOCK = new MQL4ElementType("PREPROCESSOR_BLOCK");
    IElementType PREPROCESSOR_DEFINE_BLOCK = new MQL4ElementType("PREPROCESSOR_DEFINE_BLOCK");
    IElementType PREPROCESSOR_IMPORT_BLOCK = new MQL4ElementType("PREPROCESSOR_IMPORT_BLOCK");
    IElementType PREPROCESSOR_INCLUDE_BLOCK = new MQL4ElementType("PREPROCESSOR_INCLUDE_BLOCK");
    IElementType PREPROCESSOR_PROPERTY_BLOCK = new MQL4ElementType("PREPROCESSOR_PROPERTY_BLOCK");
    IElementType PREPROCESSOR_UNDEF_BLOCK = new MQL4ElementType("PREPROCESSOR_UNDEF_BLOCK");

    IElementType TOP_LEVEL_STATEMENT = new MQL4ElementType("TOP_LEVEL_DECLARATION");

    IElementType FUNCTION_DECLARATION = new MQL4ElementType("FUNCTION_DECLARATION");
    IElementType FUNCTION_DEFINITION = new MQL4ElementType("FUNCTION_DEFINITION");
    IElementType ARGUMENTS_LIST = new MQL4ElementType("ARGUMENTS_LIST");
    IElementType ARGUMENT = new MQL4ElementType("ARGUMENT");

    IElementType CODE_BLOCK = new MQL4ElementType("CODE_BLOCK");

    IElementType EMPTY_STATEMENT = new MQL4ElementType("EMPTY_STATEMENT");
    IElementType VAR_DECLARATION_STATEMENT = new MQL4ElementType("VAR_DECLARATION_STATEMENT");
    IElementType VAR_DEFINITION_LIST = new MQL4ElementType("VAR_DEFINITION_LIST");
    IElementType VAR_DEFINITION = new MQL4ElementType("VAR_DEFINITION");

}
