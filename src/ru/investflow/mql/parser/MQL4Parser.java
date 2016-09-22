package ru.investflow.mql.parser;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.CommentParsing;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.java.parser.JavaParserUtil.error;
import static com.intellij.lang.parser.GeneratedParserUtilBase.TRUE_CONDITION;
import static com.intellij.lang.parser.GeneratedParserUtilBase._COLLAPSE_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.adapt_builder_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.functions.FunctionsParsing.FunctionParsingResult.Definition;
import static ru.investflow.mql.parser.parsing.functions.FunctionsParsing.FunctionParsingResult.Failed;
import static ru.investflow.mql.parser.parsing.functions.FunctionsParsing.parseFunction;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.parsePreprocessorBlock;
import static ru.investflow.mql.psi.MQL4Elements.TOP_LEVEL_DECLARATION;

@SuppressWarnings("SimplifiableIfStatement")
public class MQL4Parser implements PsiParser {

    @NotNull
    public ASTNode parse(@NotNull IElementType t, @NotNull PsiBuilder b0) {
        PsiBuilder b = adapt_builder_(t, b0, this);

        Marker m = enter_section_(b, 0, _COLLAPSE_, null);
        boolean r = parseFile(b, 1);
        exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);

        return b.getTreeBuilt();
    }


    /* ********************************************************** */

    static boolean parseFile(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseFile")) {
            return false;
        }
        while (!b.eof()) {
            boolean r = parseTopLevelDeclaration(b, l)
                    || CommentParsing.parseComment(b, l);
            if (!r) {
                // Show first error per line only. Skip other errors.
                error(b, "Unexpected top level statement");
                ParsingUtils.advanceLexerUntil(b, MQL4Tokens.LINE_TERMINATOR);
            }
        }
        return true;
    }

    public static boolean parseTopLevelDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseTopLevelDeclaration")) {
            return false;
        }

        Marker m = enter_section_(b);

        boolean r = parsePreprocessorBlock(b, l + 1)
                || parseFunction(b, l + 1, Definition) != Failed;

        exit_section_(b, m, TOP_LEVEL_DECLARATION, r);
        return r;
    }

}
