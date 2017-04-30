package ru.investflow.mql.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.adapt_builder_;
import static ru.investflow.mql.parser.parsing.BracketBlockParsing.parseBracketsBlock;
import static ru.investflow.mql.parser.parsing.CommentParsing.parseComment;
import static ru.investflow.mql.parser.parsing.function.EnumParsing.parseEnum;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.parseFunction;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.parsePreprocessorBlock;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseEmptyStatement;

public class MQL4Parser implements PsiParser, MQL4Elements {

    @NotNull
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder b0) {
        PsiBuilder b = adapt_builder_(root, b0, this);
        PsiBuilder.Marker rootMarker = b.mark();
        while (!b.eof()) {
            //noinspection PointlessBooleanExpression
            boolean r = false
//                    || parseVarDeclaration(b, l + 1)
                    || parseEmptyStatement(b)
                    || parseComment(b)
                    || parsePreprocessorBlock(b)
                    || parseFunction(b)
                    || parseEnum(b, 0)
                    || parseBracketsBlock(b, 0);


            //noinspection ConstantConditions
            if (!r) {
                // Show one error per line positioned on the first token only. Skip other errors until the end of line.
                b.advanceLexer();
//                b.error("Unexpected top level statement");
//                ParsingUtils.advanceLexerUntil(b, LINE_TERMINATOR, ADVANCE);
            }
        }
        rootMarker.done(root);
        return b.getTreeBuilt();
    }


    /* ********************************************************** */

    public static boolean parseIdentifier(@NotNull PsiBuilder b) {
        if (b.getTokenType() != MQL4Elements.IDENTIFIER) {
            return false;
        }
        b.advanceLexer();
        return true;
    }
}
