package ru.investflow.mql;

import org.jetbrains.annotations.NotNull;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/* Rules to highlight MQL4 Language source code.
 * Each AST element type could be colored.
 */
public class MQL4SyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey IDENTIFIER = createTextAttributesKey("MQL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey STRING = createTextAttributesKey("MQL_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey LINE_COMMENT = createTextAttributesKey("MQL_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT = createTextAttributesKey("MQL_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey INTEGER = createTextAttributesKey("MQL_INTEGER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey FLOAT = createTextAttributesKey("MQL_FLOAT", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey PREPROCESSOR_KEYWORD = createTextAttributesKey("PREPROCESSOR_KEYWORD", DefaultLanguageHighlighterColors.METADATA);
    public static final TextAttributesKey KEYWORD = createTextAttributesKey("MQL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey OPERATOR = createTextAttributesKey("MQL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("MQL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    private static final TextAttributesKey[] BAD_CHAR_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] PREPROCESSOR_KEYS = new TextAttributesKey[]{PREPROCESSOR_KEYWORD};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{LINE_COMMENT, BLOCK_COMMENT};
    private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{INTEGER, FLOAT};
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATOR};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new MQL4Lexer(null));
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (MQL4TokenSets.COMMENTS.contains(tokenType)) {
            return COMMENT_KEYS;
        } else if (MQL4TokenSets.PREPROCESSOR.contains(tokenType)) {
            return PREPROCESSOR_KEYS;
        } else if (tokenType.equals(MQL4Elements.BAD_CHARACTER)) {
            return BAD_CHAR_KEYS;
        } else if (tokenType.equals(MQL4Elements.IDENTIFIER)) {
            return IDENTIFIER_KEYS;
        } else if (MQL4TokenSets.STRINGS_AND_CHARS.contains(tokenType)) {
            return STRING_KEYS;
        } else if (MQL4TokenSets.NUMBERS.contains(tokenType)) {
            return NUMBER_KEYS;
        } else if (MQL4TokenSets.OPERATORS.contains(tokenType)) {
            return OPERATOR_KEYS;
        } else if (tokenType.toString().endsWith("_KEYWORD")) {
            return KEYWORD_KEYS;
        }
        return EMPTY_KEYS;
    }

}
