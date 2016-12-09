package ru.investflow.mql.editor;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Lexer;
import ru.investflow.mql.doc.DocEntry;
import ru.investflow.mql.doc.MQL4DocumentationProvider;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Rules to highlight MQL4 Language source code.
 */
public class MQL4SyntaxHighlighter extends SyntaxHighlighterBase {

    private static final Map<IElementType, TextAttributesKey[]> KEYS_BY_TYPE = new HashMap<>();

    static {
        put(MQL4Elements.BAD_CHARACTER, HighlighterColors.BAD_CHARACTER);
        put(MQL4Elements.BLOCK_COMMENT, DefaultLanguageHighlighterColors.BLOCK_COMMENT);
        put(MQL4Elements.IDENTIFIER, DefaultLanguageHighlighterColors.IDENTIFIER);
        put(MQL4Elements.LINE_COMMENT, DefaultLanguageHighlighterColors.LINE_COMMENT);
        put(MQL4Elements.SYNTAX_BUILT_IN_CONSTANT, DefaultLanguageHighlighterColors.CONSTANT);
        put(MQL4Elements.SYNTAX_BUILT_IN_FUNCTION, DefaultLanguageHighlighterColors.STATIC_METHOD);
        put(MQL4TokenSets.PREPROCESSOR, DefaultLanguageHighlighterColors.METADATA);
        put(MQL4TokenSets.STRINGS_AND_CHARS, DefaultLanguageHighlighterColors.STRING);
        put(MQL4TokenSets.NUMBERS, DefaultLanguageHighlighterColors.NUMBER);
        put(MQL4TokenSets.OPERATORS, DefaultLanguageHighlighterColors.OPERATION_SIGN);
    }

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new MQL4HighlighterLexer(new MQL4Lexer(null)));
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        return KEYS_BY_TYPE.computeIfAbsent(tokenType,
                t -> tokenType.toString().endsWith("_KEYWORD")
                        ? pack(DefaultLanguageHighlighterColors.KEYWORD)
                        : new TextAttributesKey[0]);
    }

    private static void put(@NotNull IElementType e, @NotNull TextAttributesKey... tokens) {
        KEYS_BY_TYPE.put(e, tokens);
    }

    private static void put(@NotNull TokenSet set, @NotNull TextAttributesKey... tokens) {
        for (IElementType e : set.getTypes()) {
            KEYS_BY_TYPE.put(e, tokens);
        }
    }

    private class MQL4HighlighterLexer implements FlexLexer {
        @NotNull
        private final MQL4Lexer lexer;

        public MQL4HighlighterLexer(@NotNull MQL4Lexer lexer) {
            this.lexer = lexer;
        }

        @Override
        public void yybegin(int state) {
            lexer.yybegin(state);
        }

        @Override
        public int yystate() {
            return lexer.yystate();
        }

        @Override
        public int getTokenStart() {
            return lexer.getTokenStart();
        }

        @Override
        public int getTokenEnd() {
            return lexer.getTokenEnd();
        }

        @Override
        public IElementType advance() throws IOException {
            IElementType originalType = lexer.advance();
            if (originalType != MQL4Elements.IDENTIFIER) {
                return originalType;
            }
            CharSequence text = lexer.yytext();
            DocEntry docEntry = MQL4DocumentationProvider.getEntryByText(text.toString());
            if (docEntry == null) {
                return originalType;
            }
            switch (docEntry.type) {
                case BuiltInConstant:
                    return MQL4Elements.SYNTAX_BUILT_IN_CONSTANT;
                case BuiltInFunction:
                    return MQL4Elements.SYNTAX_BUILT_IN_FUNCTION;
            }
            return originalType;
        }

        @Override
        public void reset(CharSequence buf, int start, int end, int initialState) {
            lexer.reset(buf, start, end, initialState);
        }
    }

    public static class MQL4SyntaxHighlighterFactory extends SyntaxHighlighterFactory {
        @NotNull
        @Override
        public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
            return new MQL4SyntaxHighlighter();
        }
    }
}
