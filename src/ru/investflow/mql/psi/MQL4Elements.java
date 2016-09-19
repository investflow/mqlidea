package ru.investflow.mql.psi;

import com.intellij.psi.tree.IElementType;

public interface MQL4Elements {

    IElementType PREPROCESSOR_BLOCK = new MQL4ElementType("PREPROCESSOR_BLOCK");
    IElementType PREPROCESSOR_DEFINE_BLOCK = new MQL4ElementType("PREPROCESSOR_DEFINE_BLOCK");
    IElementType PREPROCESSOR_IMPORT_BLOCK = new MQL4ElementType("PREPROCESSOR_IMPORT_BLOCK");
    IElementType PREPROCESSOR_INCLUDE_BLOCK = new MQL4ElementType("PREPROCESSOR_INCLUDE_BLOCK");
    IElementType PREPROCESSOR_PROPERTY_BLOCK = new MQL4ElementType("PREPROCESSOR_PROPERTY_BLOCK");
    IElementType PREPROCESSOR_UNDEF_BLOCK = new MQL4ElementType("PREPROCESSOR_UNDEF_BLOCK");

    IElementType TOP_LEVEL_DECLARATION = new MQL4ElementType("TOP_LEVEL_DECLARATION");

    IElementType FUNCTION_DECLARATION_BLOCK = new MQL4ElementType("FUNCTION_DECLARATION_BLOCK");
    IElementType FUNCTION_BLOCK = new MQL4ElementType("FUNCTION_BLOCK");
    IElementType ARGUMENTS_LIST = new MQL4ElementType("ARGUMENTS_LIST");
    IElementType ARGUMENT = new MQL4ElementType("ARGUMENT");
}
