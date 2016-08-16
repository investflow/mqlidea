package ru.investflow.mql;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4TokenTypes;
import com.intellij.psi.TokenType;

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
white_space_char = [\r\n\ \t\f]

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

<YYINITIAL> {white_space_char}+ { return TokenType.WHITE_SPACE; }
<YYINITIAL> {line_terminator}+  { return TokenType.WHITE_SPACE; }

<YYINITIAL> {block_comment}     { return MQL4TokenTypes.BLOCK_COMMENT; }
<YYINITIAL> {line_comment}      { return MQL4TokenTypes.LINE_COMMENT; }

<YYINITIAL> {char_literal} { return MQL4TokenTypes.LIT_CHAR; }
<YYINITIAL> {integer_literal} { return MQL4TokenTypes.LIT_INTEGER; }
<YYINITIAL> {float_literal} { return MQL4TokenTypes.LIT_FLOAT; }
<YYINITIAL> {double_quoted_string} { return MQL4TokenTypes.LIT_STRING; }


// https://docs.mql4.com/basis/syntax/reserved

// Data types
<YYINITIAL> "bool"      { return MQL4TokenTypes.KW_BOOL; }
<YYINITIAL> "char"      { return MQL4TokenTypes.KW_CHAR; }
<YYINITIAL> "class"     { return MQL4TokenTypes.KW_CLASS; }
<YYINITIAL> "color"     { return MQL4TokenTypes.KW_COLOR; }
<YYINITIAL> "datetime"  { return MQL4TokenTypes.KW_DATETIME; }
<YYINITIAL> "double"    { return MQL4TokenTypes.KW_DOUBLE; }

<YYINITIAL> "enum"      { return MQL4TokenTypes.KW_ENUM; }
<YYINITIAL> "float"     { return MQL4TokenTypes.KW_FLOAT; }
<YYINITIAL> "int"       { return MQL4TokenTypes.KW_INT; }
<YYINITIAL> "long"      { return MQL4TokenTypes.KW_LONG; }
<YYINITIAL> "short"     { return MQL4TokenTypes.KW_SHORT; }
<YYINITIAL> "string"    { return MQL4TokenTypes.KW_STRING; }

<YYINITIAL> "struct"    { return MQL4TokenTypes.KW_STRUCT; }
<YYINITIAL> "uchar"     { return MQL4TokenTypes.KW_UCHAR; }
<YYINITIAL> "uint"      { return MQL4TokenTypes.KW_UINT; }
<YYINITIAL> "ulong"     { return MQL4TokenTypes.KW_ULONG; }
<YYINITIAL> "ushort"    { return MQL4TokenTypes.KW_USHORT; }
<YYINITIAL> "void"      { return MQL4TokenTypes.KW_VOID; }

// Access specificators
<YYINITIAL> "const"     { return MQL4TokenTypes.KW_CONST; }
<YYINITIAL> "public"    { return MQL4TokenTypes.KW_PUBLIC; }
<YYINITIAL> "private"   { return MQL4TokenTypes.KW_PRIVATE; }
<YYINITIAL> "virtual"   { return MQL4TokenTypes.KW_VIRTUAL; }
<YYINITIAL> "protected" { return MQL4TokenTypes.KW_PROTECTED; }

// Memory classes
<YYINITIAL> "extern"    { return MQL4TokenTypes.KW_EXTERN; }
<YYINITIAL> "input"     { return MQL4TokenTypes.KW_INPUT; }
<YYINITIAL> "static"    { return MQL4TokenTypes.KW_STATIC; }

// Operators
<YYINITIAL> "break"     { return MQL4TokenTypes.KW_BREAK; }
<YYINITIAL> "case"      { return MQL4TokenTypes.KW_CASE; }
<YYINITIAL> "continue"  { return MQL4TokenTypes.KW_CONTINUE; }
<YYINITIAL> "default"   { return MQL4TokenTypes.KW_DEFAULT; }
<YYINITIAL> "delete"    { return MQL4TokenTypes.KW_DELETE; }

<YYINITIAL> "do"        { return MQL4TokenTypes.KW_DO; }
<YYINITIAL> "else"      { return MQL4TokenTypes.KW_ELSE; }
<YYINITIAL> "for"       { return MQL4TokenTypes.KW_FOR; }
<YYINITIAL> "if"        { return MQL4TokenTypes.KW_IF; }
<YYINITIAL> "new"       { return MQL4TokenTypes.KW_NEW; }

<YYINITIAL> "operator"  { return MQL4TokenTypes.KW_OPERATOR; }
<YYINITIAL> "return"    { return MQL4TokenTypes.KW_RETURN; }
<YYINITIAL> "sizeof"    { return MQL4TokenTypes.KW_SIZEOF; }
<YYINITIAL> "switch"    { return MQL4TokenTypes.KW_SWITCH; }
<YYINITIAL> "while"     { return MQL4TokenTypes.KW_WHILE; }

// Other
<YYINITIAL> "false"     { return MQL4TokenTypes.KW_FALSE; }
<YYINITIAL> "this"      { return MQL4TokenTypes.KW_THIS; }
<YYINITIAL> "true"      { return MQL4TokenTypes.KW_TRUE; }
<YYINITIAL> "strict"    { return MQL4TokenTypes.KW_STRICT; }

//<YYINITIAL> "#define"   { return MQL4TokenTypes.KW_XDEFINE; }
//<YYINITIAL> "#import"   { return MQL4TokenTypes.KW_XIMPORT; }
//<YYINITIAL> "#include"  { return MQL4TokenTypes.KW_XINCLUDE; }

<YYINITIAL> "#property" { return MQL4TokenTypes.KW_XPROPERTY; }
//<YYINITIAL> "template"  { return MQL4TokenTypes.KW_TEMPLATE; }
//<YYINITIAL> "typename"  { return MQL4TokenTypes.KW_TYPENAME; }

//<YYINITIAL> "\.\.\."  { return MQL4TokenTypes.OP_TRIPLEDOT; }
//<YYINITIAL> "\.\."    { return MQL4TokenTypes.OP_DDOT; }
//<YYINITIAL> \.        { return MQL4TokenTypes.OP_DOT; }
<YYINITIAL> ";"       { return MQL4TokenTypes.OP_SCOLON; }
<YYINITIAL> ":"       { return MQL4TokenTypes.OP_COLON; }
<YYINITIAL> ","       { return MQL4TokenTypes.OP_COMMA; }
//
<YYINITIAL> "\+="     { return MQL4TokenTypes.OP_PLUS_EQ; }
<YYINITIAL> "\-="     { return MQL4TokenTypes.OP_MINUS_EQ; }
<YYINITIAL> "\*="     { return MQL4TokenTypes.OP_MUL_EQ; }
<YYINITIAL> "\/="     { return MQL4TokenTypes.OP_DIV_EQ; }
<YYINITIAL> "\%="     { return MQL4TokenTypes.OP_MOD_EQ; }
<YYINITIAL> "\&="     { return MQL4TokenTypes.OP_AND_EQ; }
<YYINITIAL> "\|="     { return MQL4TokenTypes.OP_OR_EQ; }
<YYINITIAL> "\^="     { return MQL4TokenTypes.OP_XOR_EQ; }
<YYINITIAL> "\~="     { return MQL4TokenTypes.OP_TILDA_EQ; }
<YYINITIAL> "\<\<="   { return MQL4TokenTypes.OP_SH_LEFT_EQ; }
<YYINITIAL> "\>\>="   { return MQL4TokenTypes.OP_SH_RIGHT_EQ; }
<YYINITIAL> "\>\>\>=" { return MQL4TokenTypes.OP_USH_RIGHT_EQ; }
<YYINITIAL> "\^\^="   { return MQL4TokenTypes.OP_POW_EQ; }
//
<YYINITIAL> "\+\+"    { return MQL4TokenTypes.OP_PLUS_PLUS; }
<YYINITIAL> "\-\-"    { return MQL4TokenTypes.OP_MINUS_MINUS; }
<YYINITIAL> "\|\|"    { return MQL4TokenTypes.OP_BOOL_OR; }
<YYINITIAL> "\&\&"    { return MQL4TokenTypes.OP_BOOL_AND; }
<YYINITIAL> "\>\>\>"  { return MQL4TokenTypes.OP_USH_RIGHT; }
<YYINITIAL> "\>\>"    { return MQL4TokenTypes.OP_SH_RIGHT; }
<YYINITIAL> "\<\<"    { return MQL4TokenTypes.OP_SH_LEFT; }
<YYINITIAL> "\^\^"    { return MQL4TokenTypes.OP_POW; }
<YYINITIAL> "=="      { return MQL4TokenTypes.OP_EQ_EQ; }
<YYINITIAL> "="       { return MQL4TokenTypes.OP_EQ; }
<YYINITIAL> "\<="     { return MQL4TokenTypes.OP_LESS_EQ; }
<YYINITIAL> "\>="     { return MQL4TokenTypes.OP_GT_EQ; }
<YYINITIAL> "\!="     { return MQL4TokenTypes.OP_NOT_EQ; }
//
<YYINITIAL> "\|"      { return MQL4TokenTypes.OP_OR; }
<YYINITIAL> "\^"      { return MQL4TokenTypes.OP_XOR; }
<YYINITIAL> "\+"      { return MQL4TokenTypes.OP_PLUS; }
<YYINITIAL> "\-"      { return MQL4TokenTypes.OP_MINUS; }
<YYINITIAL> "\*"      { return MQL4TokenTypes.OP_MUL; }
<YYINITIAL> "\~"      { return MQL4TokenTypes.OP_TILDA; }
<YYINITIAL> "\/"      { return MQL4TokenTypes.OP_DIV; }
<YYINITIAL> "\%"      { return MQL4TokenTypes.OP_MOD; }
<YYINITIAL> "\&"      { return MQL4TokenTypes.OP_AND; }
<YYINITIAL> "\!"      { return MQL4TokenTypes.OP_NOT; }
<YYINITIAL> "\?"      { return MQL4TokenTypes.OP_QUESTION; }
<YYINITIAL> "\<"      { return MQL4TokenTypes.OP_LESS; }
<YYINITIAL> "\>"      { return MQL4TokenTypes.OP_GT; }

//<YYINITIAL> "\("      { return MQL4TokenTypes.OP_PAR_LEFT; }
//<YYINITIAL> "\)"      { return MQL4TokenTypes.OP_PAR_RIGHT; }
//<YYINITIAL> "\["      { return MQL4TokenTypes.OP_BRACKET_LEFT; }
//<YYINITIAL> "\]"      { return MQL4TokenTypes.OP_BRACKET_RIGHT; }
//<YYINITIAL> "\{"      { return MQL4TokenTypes.OP_BRACES_LEFT; }
//<YYINITIAL> "\}"      { return MQL4TokenTypes.OP_BRACES_RIGHT; }
<YYINITIAL> {identifier}        { return MQL4TokenTypes.ID; }

. { return TokenType.BAD_CHARACTER; }
