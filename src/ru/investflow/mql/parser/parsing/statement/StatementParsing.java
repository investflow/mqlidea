package ru.investflow.mql.parser.parsing.statement;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.Definition;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.Failed;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.parseFunction;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.parsePreprocessorBlock;
import static ru.investflow.mql.parser.parsing.statement.VarDeclarationStatement.parseVarDeclaration;
import static ru.investflow.mql.psi.MQL4Elements.TOP_LEVEL_STATEMENT;

public class StatementParsing {

    public static boolean parseTopLevelStatement(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseTopLevelStatement")) {
            return false;
        }

        PsiBuilder.Marker m = enter_section_(b);

        boolean r = parsePreprocessorBlock(b, l + 1)
                || parseFunction(b, l + 1, Definition) != Failed
                || parseVarDeclaration(b, l + 1)
                || parseEmptyStatement(b);


        exit_section_(b, m, TOP_LEVEL_STATEMENT, r);
        return r;
    }

    public static boolean parseStatement(PsiBuilder b, int l) {
        //noinspection SimplifiableIfStatement
        if (!recursion_guard_(b, l, "parseStatement")) {
            return false;
        }

        return parseEmptyStatement(b) || parseVarDeclaration(b, l);
    }

    public static boolean parseEmptyStatement(PsiBuilder b) {
        IElementType t = b.getTokenType();
        if (t != MQL4Tokens.SEMICOLON) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer();
        exit_section_(b, m, MQL4Elements.EMPTY_STATEMENT, true);
        return true;
    }


}
