package ru.investflow.mql.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.MQL4Lexer;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4ElementsFactory;
import ru.investflow.mql.psi.MQL4File;
import ru.investflow.mql.psi.stub.MQL4StubElements;

/* Parser definition used by IntelliJ Platform to parse MQL4 Language sources. */
public class MQL4ParserDefinition implements ParserDefinition, MQL4Elements {

    public static final TokenSet WHITE_SPACES = TokenSet.create(WHITE_SPACE, LINE_TERMINATOR);

    public static final TokenSet COMMENTS = TokenSet.create(BLOCK_COMMENT, LINE_COMMENT);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new FlexAdapter(new MQL4Lexer(null));
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new MQL4Parser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return MQL4StubElements.FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new MQL4File(viewProvider);
    }

    @NotNull
    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return MQL4ElementsFactory.createElement(node);
    }
}
