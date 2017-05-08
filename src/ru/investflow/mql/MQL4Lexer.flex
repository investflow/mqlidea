package ru.investflow.mql;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4Elements;

@SuppressWarnings({"ALL"})
%%

%public %class MQL4Lexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

line_terminator = \r|\n|\r\n
white_space_char = [ \t\f]

letter = [:letter:]
digit = [:digit:]

identifier = (_|{letter}) (_|{digit}|{letter})*

block_comment = "/*" {block_comment_content} "*/"
block_comment_content = ([^*] | "*"+ [^/])*
line_comment = "/""/"[^\r\n]* {line_terminator}?

double_quoted_string = \" ( [^\\\"] |{escape_sequence})* \" {string_postfix}?
string_postfix = [cwd]
include_quoted_string = "<" ({letter} | {digit} | "." | "/" | "\\" | "-" | "_" | "$")* ">"

escape_sequence = {escape_sequence_spec_char}
escape_sequence_spec_char = "\\\'" | "\\\"" | "\\\?" | "\\\\" | "\\0" | "\\a" | "\\b"  | "\\f"  | "\\n"  | "\\r"  | "\\t" | "\\v"

char_literal = \' ( [^\r\n\t\f\\] | {escape_sequence} ) \'
integer_literal = ({decimal_integer} | {hexadecimal_integer}) {integer_suffix}?

integer_suffix =  L | u | U | Lu | LU | uL | UL
decimal_integer = 0 | ([1-9] [0-9]*)
hexadecimal_integer = 0[xX] [0-9a-fA-F] [0-9a-fA-F]*

float_literal = {decimal_float}
decimal_float = ( {decimal_float_simple} | {decimal_float_exponent} | {decimal_float_no_dot_exponent} )
decimal_float_simple = [0-9]* \. ([0-9]+)
decimal_float_exponent = [0-9]* \. [0-9_]+ {decimal_exponent}
decimal_float_no_dot_exponent = [0-9]+ {decimal_exponent}
decimal_exponent = [eE][\+\-]? [0-9_]+

color_prefix = clr
color_name= Black | DarkGreen | DarkSlateGray | Olive | Green | Teal | Navy | Purple | Maroon | Indigo | MidnightBlue | DarkBlue | DarkOliveGreen
             | SaddleBrown | ForestGreen | OliveDrab | SeaGreen | DarkGoldenrod | DarkSlateBlue | Sienna | MediumBlue | Brown | DarkTurquoise | DimGray
             | LightSeaGreen| DarkViolet | FireBrick | MediumVioletRed | MediumSeaGreen | Chocolate | Crimson | SteelBlue | Goldenrod | MediumSpringGreen
             | LawnGreen | CadetBlue | DarkOrchid | YellowGreen | LimeGreen | OrangeRed | DarkOrange | Orange | Gold | Yellow | Chartreuse | Lime
             | SpringGreen | Aqua | DeepSkyBlue | Blue | Magenta | Red | Gray | SlateGray | Peru | BlueViolet | LightSlateGray | DeepPink
             | MediumTurquoise | DodgerBlue | Turquoise | RoyalBlue | SlateBlue | DarkKhaki | IndianRed | MediumOrchid | GreenYellow | MediumAquamarine
             | DarkSeaGreen | Tomato | RosyBrown | Orchid | MediumPurple | PaleVioletRed | Coral | CornflowerBlue | DarkGray | SandyBrown | MediumSlateBlue
             | Tan | DarkSalmon | BurlyWood | HotPink | Salmon | Violet | LightCoral | SkyBlue | LightSalmon | Plum | Khaki | LightGreen | Aquamarine
             | Silver | LightSkyBlue | LightSteelBlue | LightBlue | PaleGreen | Thistle | PowderBlue | PaleGoldenrod | PaleTurquoise | LightGray | Wheat
             | NavajoWhite | Moccasin | LightPink | Gainsboro | PeachPuff | Pink | Bisque | LightGoldenrod | BlanchedAlmond | LemonChiffon | Beige 
             | AntiqueWhite | PapayaWhip | Cornsilk | LightYellow | LightCyan | Linen | Lavender | MistyRose | OldLace | WhiteSmoke | Seashell |  Ivory
             | Honeydew | AliceBlue | LavenderBlush | MintCream | Snow | White

color_constant_literal = {color_prefix} {color_name} | {color_name}
color_string_literal = C \' ({decimal_integer} | {hexadecimal_integer} | , )* \'

/*End of rules*/

%%
<YYINITIAL> {

{line_terminator}+  { return MQL4Elements.LINE_TERMINATOR; }
{white_space_char}+ { return MQL4Elements.WHITE_SPACE; }

{block_comment}     { return MQL4Elements.BLOCK_COMMENT; }
{line_comment}      { return MQL4Elements.LINE_COMMENT; }

{color_string_literal}     { return MQL4Elements.COLOR_STRING_LITERAL;}
{char_literal} { return MQL4Elements.CHAR_LITERAL; }
{integer_literal} { return MQL4Elements.INTEGER_LITERAL; }
{float_literal} { return MQL4Elements.DOUBLE_LITERAL; }
{double_quoted_string} { return MQL4Elements.STRING_LITERAL; }
{include_quoted_string} { return MQL4Elements.INCLUDE_STRING_LITERAL; }
{color_constant_literal} { return MQL4Elements.COLOR_CONSTANT_LITERAL; }



// Keywords

// https://docs.mql4.com/basis/syntax/reserved

// Data types
"bool"      { return MQL4Elements.BOOL_KEYWORD; }
"char"      { return MQL4Elements.CHAR_KEYWORD; }
"class"     { return MQL4Elements.CLASS_KEYWORD; }
"color"     { return MQL4Elements.COLOR_KEYWORD; }
"datetime"  { return MQL4Elements.DATETIME_KEYWORD; }
"double"    { return MQL4Elements.DOUBLE_KEYWORD; }
"enum"      { return MQL4Elements.ENUM_KEYWORD; }
"float"     { return MQL4Elements.FLOAT_KEYWORD; }
"int"       { return MQL4Elements.INT_KEYWORD; }
"interface" { return MQL4Elements.INTERFACE_KEYWORD; }
"long"      { return MQL4Elements.LONG_KEYWORD; }
"short"     { return MQL4Elements.SHORT_KEYWORD; }
"string"    { return MQL4Elements.STRING_KEYWORD; }
"struct"    { return MQL4Elements.STRUCT_KEYWORD; }
"uchar"     { return MQL4Elements.UCHAR_KEYWORD; }
"uint"      { return MQL4Elements.UINT_KEYWORD; }
"ulong"     { return MQL4Elements.ULONG_KEYWORD; }
"ushort"    { return MQL4Elements.USHORT_KEYWORD; }
"void"      { return MQL4Elements.VOID_KEYWORD; }


// Access specificators
"const"     { return MQL4Elements.CONST_KEYWORD; }
"public"    { return MQL4Elements.PUBLIC_KEYWORD; }
"private"   { return MQL4Elements.PRIVATE_KEYWORD; }
"virtual"   { return MQL4Elements.VIRTUAL_KEYWORD; }
"protected" { return MQL4Elements.PROTECTED_KEYWORD; }

// Memory classes
"extern"    { return MQL4Elements.EXTERN_KEYWORD; }
"input"     { return MQL4Elements.INPUT_KEYWORD; }
"static"    { return MQL4Elements.STATIC_KEYWORD; }

// Operators
"break"     { return MQL4Elements.BREAK_KEYWORD; }
"continue"  { return MQL4Elements.CONTINUE_KEYWORD; }
"delete"    { return MQL4Elements.DELETE_KEYWORD; }
"new"       { return MQL4Elements.NEW_KEYWORD; }

"if"        { return MQL4Elements.IF_KEYWORD; }
"else"      { return MQL4Elements.ELSE_KEYWORD; }

"do"        { return MQL4Elements.DO_KEYWORD; }
"while"     { return MQL4Elements.WHILE_KEYWORD; }
"for"       { return MQL4Elements.FOR_KEYWORD; }

"switch"    { return MQL4Elements.SWITCH_KEYWORD; }
"case"      { return MQL4Elements.CASE_KEYWORD; }
"default"   { return MQL4Elements.DEFAULT_KEYWORD; }

"operator"  { return MQL4Elements.OPERATOR_KEYWORD; }
"return"    { return MQL4Elements.RETURN_KEYWORD; }
"sizeof"    { return MQL4Elements.SIZEOF_KEYWORD; }

// Other
"false"     { return MQL4Elements.FALSE_KEYWORD; }
"true"      { return MQL4Elements.TRUE_KEYWORD; }
"this"      { return MQL4Elements.THIS_KEYWORD; }

"#define"   { return MQL4Elements.DEFINE_KEYWORD; }
"#undef"    { return MQL4Elements.UNDEF_KEYWORD; }
"#ifdef"    { return MQL4Elements.IFDEF_KEYWORD; }
"#endif"    { return MQL4Elements.ENDIF_KEYWORD; }
"#import"   { return MQL4Elements.IMPORT_KEYWORD; }
"#include"  { return MQL4Elements.INCLUDE_KEYWORD; }
"#property" { return MQL4Elements.PROPERTY_KEYWORD; }
"#resource" { return MQL4Elements.RESOURCE_KEYWORD; }

"template"  { return MQL4Elements.TEMPLATE_KEYWORD; }
"typename"  { return MQL4Elements.TYPENAME_KEYWORD; }

{identifier}    { return MQL4Elements.IDENTIFIER; }

// Operators & special characters

//"\.\.\."  { return MQL4Elements.OP_TRIPLEDOT; }
//"\.\."    { return MQL4Elements.OP_DDOT; }
//\.        { return MQL4Elements.OP_DOT; }

"+="     { return MQL4Elements.PLUS_EQ; }
"-="     { return MQL4Elements.MINUS_EQ; }
"*="     { return MQL4Elements.MUL_EQ; }
"/="     { return MQL4Elements.DIV_EQ; }
"%="     { return MQL4Elements.MOD_EQ; }
"&="     { return MQL4Elements.AND_EQ; }
"|="     { return MQL4Elements.OR_EQ; }
"^="     { return MQL4Elements.XOR_EQ; }
"~="     { return MQL4Elements.TILDA_EQ; }
"<<="    { return MQL4Elements.SH_LEFT_EQ; }
">>="    { return MQL4Elements.SH_RIGHT_EQ; }

"++"    { return MQL4Elements.PLUS_PLUS; }
"--"    { return MQL4Elements.MINUS_MINUS; }
"||"    { return MQL4Elements.BOOL_OR; }
"&&"    { return MQL4Elements.BOOL_AND; }
">>"    { return MQL4Elements.SH_RIGHT; }
"<<"    { return MQL4Elements.SH_LEFT; }
"=="    { return MQL4Elements.EQ_EQ; }
"="     { return MQL4Elements.EQ; }
"<="    { return MQL4Elements.LESS_EQ; }
">="    { return MQL4Elements.GT_EQ; }
"!="    { return MQL4Elements.NOT_EQ; }

"|"      { return MQL4Elements.OR; }
"^"      { return MQL4Elements.XOR; }
"+"      { return MQL4Elements.PLUS; }
"-"      { return MQL4Elements.MINUS; }
"*"      { return MQL4Elements.MUL; }
"~"      { return MQL4Elements.TILDA; }
"/"      { return MQL4Elements.DIV; }
"%"      { return MQL4Elements.MOD; }
"&"      { return MQL4Elements.AND; }
"!"      { return MQL4Elements.NOT; }
"?"      { return MQL4Elements.QUESTION; }

"<"   { return MQL4Elements.LT; }
">"   { return MQL4Elements.GT; }
"("   { return MQL4Elements.L_ROUND_BRACKET; }
")"   { return MQL4Elements.R_ROUND_BRACKET; }
"{"   { return MQL4Elements.L_CURLY_BRACKET; }
"}"   { return MQL4Elements.R_CURLY_BRACKET; }
"["   { return MQL4Elements.L_SQUARE_BRACKET; }
"]"   { return MQL4Elements.R_SQUARE_BRACKET; }
";"   { return MQL4Elements.SEMICOLON; }
"::"   { return MQL4Elements.COLON_COLON; }
":"   { return MQL4Elements.COLON; }
","   { return MQL4Elements.COMMA; }
"."   { return MQL4Elements.DOT; }
}

[^] { return MQL4Elements.BAD_CHARACTER; }
