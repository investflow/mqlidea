package ru.investflow.mql.parser.parsing.statement;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.parser.parsing.util.TokenAdvanceMode;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.statement.LoopStatementParsing.parseLoop;
import static ru.investflow.mql.parser.parsing.statement.VarDeclarationStatement.parseVarDeclaration;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;

public class StatementParsing implements MQL4Elements {

    private static final TokenSet SINGLE_WORD_STATEMENTS = TokenSet.create(
            BREAK_KEYWORD,
            CONTINUE_KEYWORD
    );

    public static boolean parseStatementOrFail(PsiBuilder b, int l) {
        if (parseStatement(b, l)) {
            return true;
        }
        b.error("Statement expected");
        return false;
    }

    public static boolean parseStatement(PsiBuilder b, int l) {
        //noinspection SimplifiableIfStatement
        if (!recursion_guard_(b, l, "parseStatement")) {
            return false;
        }

        return parseEmptyStatement(b)
                || parseVarDeclaration(b, l)
                || parseSingleWordStatement(b)
                || parseLoop(b, l);
    }

    private static boolean parseSingleWordStatement(PsiBuilder b) {
        IElementType t = b.getTokenType();
        if (!SINGLE_WORD_STATEMENTS.contains(t)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer();
        if (!parseTokenOrFail(b, SEMICOLON)) {
            ParsingUtils.advanceLexerUntil(b, SEMICOLON, TokenAdvanceMode.ADVANCE);
        }
        exit_section_(b, m, SINGLE_WORD_STATEMENT, true);
        return true;
    }

    public static boolean parseEmptyStatement(PsiBuilder b) {
        IElementType t = b.getTokenType();
        if (t != SEMICOLON) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer();
        exit_section_(b, m, EMPTY_STATEMENT, true);
        return true;
    }

}
