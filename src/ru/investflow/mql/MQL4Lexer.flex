package ru.investflow.mql;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4Tokens;

@SuppressWarnings({"ALL"})
%%

%class MQL4Lexer
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

escape_sequence = {escape_sequence_spec_char}
escape_sequence_spec_char = "\\\'" | "\\\"" | "\\\?" | "\\\\" | "\\0" | "\\a"
                          | "\\b"  | "\\f"  | "\\n"  | "\\r"  | "\\t" | "\\v"

char_literal = \' ( [^\r\n\t\f\\] | {escape_sequence} ) \'
integer_literal = ({decimal_integer} | {hexadecimal_integer}) {integer_suffix}?

integer_suffix =  L | u | U | Lu | LU | uL | UL
decimal_integer = 0 | ([1-9] [0-9_]*)
hexadecimal_integer = 0[xX] [0-9a-fA-F] [0-9a-fA-F_]*


float_literal = {decimal_float}
decimal_float = ( {decimal_float_simple} | {decimal_float_exponent} | {decimal_float_no_dot_exponent} )
decimal_float_simple = [0-9]* \. ([0-9]*)
decimal_float_exponent = [0-9]* \. [0-9_]+ {decimal_exponent}
decimal_float_no_dot_exponent = [0-9]+ {decimal_exponent}
decimal_exponent = [eE][\+\-]? [0-9_]+

/*End of rules*/

%%
<YYINITIAL> {

{line_terminator}+  { return MQL4Tokens.LINE_TERMINATOR; }
{white_space_char}+ { return MQL4Tokens.WHITE_SPACE; }

{block_comment}     { return MQL4Tokens.BLOCK_COMMENT; }
{line_comment}      { return MQL4Tokens.LINE_COMMENT; }


// https://docs.mql4.com/basis/syntax/reserved

// Data types
//"bool"      { return MQL4Tokens.KW_BOOL; }
//"char"      { return MQL4Tokens.KW_CHAR; }
//"class"     { return MQL4Tokens.KW_CLASS; }
//"color"     { return MQL4Tokens.KW_COLOR; }
//"datetime"  { return MQL4Tokens.KW_DATETIME; }
//"double"    { return MQL4Tokens.KW_DOUBLE; }
  //"enum"      { return MQL4Tokens.KW_ENUM; }
//"float"     { return MQL4Tokens.KW_FLOAT; }
//"int"       { return MQL4Tokens.KW_INT; }
//"long"      { return MQL4Tokens.KW_LONG; }
//"short"     { return MQL4Tokens.KW_SHORT; }
//"string"    { return MQL4Tokens.KW_STRING; }
////"struct"    { return MQL4Tokens.KW_STRUCT; }
//"uchar"     { return MQL4Tokens.KW_UCHAR; }
//"uint"      { return MQL4Tokens.KW_UINT; }
//"ulong"     { return MQL4Tokens.KW_ULONG; }
//"ushort"    { return MQL4Tokens.KW_USHORT; }
//"void"      { return MQL4Tokens.KW_VOID; }
//
// Access specificators
//"const"     { return MQL4Tokens.KW_CONST; }
//"public"    { return MQL4Tokens.KW_PUBLIC; }
//"private"   { return MQL4Tokens.KW_PRIVATE; }
//"virtual"   { return MQL4Tokens.KW_VIRTUAL; }
//"protected" { return MQL4Tokens.KW_PROTECTED; }
//
// Memory classes
//"extern"    { return MQL4Tokens.KW_EXTERN; }
//"input"     { return MQL4Tokens.KW_INPUT; }
//"static"    { return MQL4Tokens.KW_STATIC; }
//
// Operators
//"break"     { return MQL4Tokens.KW_BREAK; }
//"case"      { return MQL4Tokens.KW_CASE; }
//"continue"  { return MQL4Tokens.KW_CONTINUE; }
//"default"   { return MQL4Tokens.KW_DEFAULT; }
//"delete"    { return MQL4Tokens.KW_DELETE; }
//
//"do"        { return MQL4Tokens.KW_DO; }
//"else"      { return MQL4Tokens.KW_ELSE; }
//"for"       { return MQL4Tokens.KW_FOR; }
//"if"        { return MQL4Tokens.KW_IF; }
//"new"       { return MQL4Tokens.KW_NEW; }
//
//"operator"  { return MQL4Tokens.KW_OPERATOR; }
//"return"    { return MQL4Tokens.KW_RETURN; }
//"sizeof"    { return MQL4Tokens.KW_SIZEOF; }
//"switch"    { return MQL4Tokens.KW_SWITCH; }
//"while"     { return MQL4Tokens.KW_WHILE; }
//
// Other
//"false"     { return MQL4Tokens.KW_FALSE; }
//"this"      { return MQL4Tokens.KW_THIS; }
//"true"      { return MQL4Tokens.KW_TRUE; }
//"strict"    { return MQL4Tokens.KW_STRICT; }

"#define"   { return MQL4Tokens.DEFINE_KEYWORD; }
"#undef"   { return MQL4Tokens.UNDEF_KEYWORD; }
//"#import"   { return MQL4Tokens.KW_XIMPORT; }
//"#include"  { return MQL4Tokens.KW_XINCLUDE; }

"#property" { return MQL4Tokens.PROPERTY_KEYWORD; }
//"template"  { return MQL4Tokens.KW_TEMPLATE; }
//"typename"  { return MQL4Tokens.KW_TYPENAME; }

//"\.\.\."  { return MQL4Tokens.OP_TRIPLEDOT; }
//"\.\."    { return MQL4Tokens.OP_DDOT; }
//\.        { return MQL4Tokens.OP_DOT; }
//";"       { return MQL4Tokens.OP_SCOLON; }
//":"       { return MQL4Tokens.OP_COLON; }
//","       { return MQL4Tokens.OP_COMMA; }
//
//"\+="     { return MQL4Tokens.OP_PLUS_EQ; }
//"\-="     { return MQL4Tokens.OP_MINUS_EQ; }
//"\*="     { return MQL4Tokens.OP_MUL_EQ; }
//"\/="     { return MQL4Tokens.OP_DIV_EQ; }
//"\%="     { return MQL4Tokens.OP_MOD_EQ; }
//"\&="     { return MQL4Tokens.OP_AND_EQ; }
//"\|="     { return MQL4Tokens.OP_OR_EQ; }
//"\^="     { return MQL4Tokens.OP_XOR_EQ; }
//"\~="     { return MQL4Tokens.OP_TILDA_EQ; }
//"\<\<="   { return MQL4Tokens.OP_SH_LEFT_EQ; }
//"\>\>="   { return MQL4Tokens.OP_SH_RIGHT_EQ; }
//"\>\>\>=" { return MQL4Tokens.OP_USH_RIGHT_EQ; }
//"\^\^="   { return MQL4Tokens.OP_POW_EQ; }

//"\+\+"    { return MQL4Tokens.OP_PLUS_PLUS; }
//"\-\-"    { return MQL4Tokens.OP_MINUS_MINUS; }
//"\|\|"    { return MQL4Tokens.OP_BOOL_OR; }
//"\&\&"    { return MQL4Tokens.OP_BOOL_AND; }
//"\>\>\>"  { return MQL4Tokens.OP_USH_RIGHT; }
//"\>\>"    { return MQL4Tokens.OP_SH_RIGHT; }
//"\<\<"    { return MQL4Tokens.OP_SH_LEFT; }
//"\^\^"    { return MQL4Tokens.OP_POW; }
//"=="      { return MQL4Tokens.OP_EQ_EQ; }
//"="       { return MQL4Tokens.OP_EQ; }
//"\<="     { return MQL4Tokens.OP_LESS_EQ; }
//"\>="     { return MQL4Tokens.OP_GT_EQ; }
//"\!="     { return MQL4Tokens.OP_NOT_EQ; }

//"\|"      { return MQL4Tokens.OP_OR; }
//"\^"      { return MQL4Tokens.OP_XOR; }
//"\+"      { return MQL4Tokens.OP_PLUS; }
//"\-"      { return MQL4Tokens.OP_MINUS; }
//"\*"      { return MQL4Tokens.OP_MUL; }
//"\~"      { return MQL4Tokens.OP_TILDA; }
//"\/"      { return MQL4Tokens.OP_DIV; }
//"\%"      { return MQL4Tokens.OP_MOD; }
//"\&"      { return MQL4Tokens.OP_AND; }
//"\!"      { return MQL4Tokens.OP_NOT; }
//"\?"      { return MQL4Tokens.OP_QUESTION; }
//"\<"      { return MQL4Tokens.OP_LESS; }
//"\>"      { return MQL4Tokens.OP_GT; }
//
"("   { return MQL4Tokens.LPARENTH; }
")"   { return MQL4Tokens.RPARENTH; }
  //"\["      { return MQL4Tokens.OP_BRACKET_LEFT; }
//"\]"      { return MQL4Tokens.OP_BRACKET_RIGHT; }
//"\{"      { return MQL4Tokens.OP_BRACES_LEFT; }
//"\}"      { return MQL4Tokens.OP_BRACES_RIGHT; }


{char_literal} { return MQL4Tokens.CHAR_LITERAL; }
{integer_literal} { return MQL4Tokens.INTEGER_LITERAL; }
{float_literal} { return MQL4Tokens.DOUBLE_LITERAL; }
{double_quoted_string} { return MQL4Tokens.STRING_LITERAL; }

{identifier}        { return MQL4Tokens.IDENTIFIER; }
}

[^] { return MQL4Tokens.BAD_CHARACTER; }