package ru.investflow.mql.parser.parsing.statement;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.parsing.CodeBlockParsing;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.parser.parsing.util.TokenAdvanceMode;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.CodeBlockParsing.parseCodeBlock;
import static ru.investflow.mql.parser.parsing.ExpressionParsing.parseExpressionOrFail;
import static ru.investflow.mql.parser.parsing.statement.StatementParsing.parseStatementOrFail;
import static ru.investflow.mql.parser.parsing.statement.VarDeclarationStatement.parseEmbeddedVarAssignmentsListOrFail;
import static ru.investflow.mql.parser.parsing.statement.VarDeclarationStatement.parseEmbeddedVarDeclarationOrAssignmentOrFail;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.STATEMENT_TERMINATORS;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseKeywordOrFail;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;

public class LoopStatementParsing implements MQL4Elements {

    static boolean parseLoop(PsiBuilder b, int l) {
        return recursion_guard_(b, l, "parseLoop")
                && (parseForLoop(b, l) || parseDoLoop(b, l) || parseWhileLoop(b, l));
    }


    private static boolean parseForLoop(PsiBuilder b, int l) {
        if (b.getTokenType() != FOR_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer(); // 'for'
        try {
            boolean ok = parseTokenOrFail(b, LPARENTH) // '('
                    && parseEmbeddedVarDeclarationOrAssignmentOrFail(b, l + 1, FOR_LOOP_SECTION_1, VAR_ASSIGNMENT_LIST)
                    && parseTokenOrFail(b, SEMICOLON) // ';'
                    && parseExpressionOrFail(b, l + 1, true)
                    && parseTokenOrFail(b, SEMICOLON) // ';'
                    && parseEmbeddedVarAssignmentsListOrFail(b, l + 1, VAR_ASSIGNMENT_LIST, RPARENTH)
                    && parseTokenOrFail(b, RPARENTH) // ')'
                    && (parseCodeBlock(b, l + 1) || parseStatementOrFail(b, l + 1)); // '{}'

            if (!ok) {
                ParsingUtils.advanceLexerUntil(b, STATEMENT_TERMINATORS, TokenAdvanceMode.ADVANCE);
            }
            return true;
        } finally {
            exit_section_(b, m, FOR_LOOP, true);
        }
    }

    private static boolean parseWhileLoop(PsiBuilder b, int l) {
        if (b.getTokenType() != WHILE_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer(); // 'while'
        try {
            boolean ok = parseTokenOrFail(b, LPARENTH) // '('
                    && parseExpressionOrFail(b, l + 1)
                    && parseTokenOrFail(b, RPARENTH) // ')'
                    && (parseCodeBlock(b, l + 1) || parseStatementOrFail(b, l + 1)); // '{}'

            if (!ok) {
                ParsingUtils.advanceLexerUntil(b, STATEMENT_TERMINATORS, TokenAdvanceMode.ADVANCE);
            }
            return true;

        } finally {
            exit_section_(b, m, WHILE_LOOP, true);
        }
    }

    private static boolean parseDoLoop(PsiBuilder b, int l) {
        if (b.getTokenType() != DO_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer(); // 'do'
        try {
            boolean ok = CodeBlockParsing.parseCodeBlockOrFail(b, l + 1)   // '{}'
                    && parseKeywordOrFail(b, WHILE_KEYWORD)  // 'while'
                    && parseTokenOrFail(b, LPARENTH)  // '('
                    && parseExpressionOrFail(b, l + 1)
                    && parseTokenOrFail(b, RPARENTH)  // ')'
                    && parseTokenOrFail(b, SEMICOLON); // ';'
            if (!ok) {
                ParsingUtils.advanceLexerUntil(b, STATEMENT_TERMINATORS, TokenAdvanceMode.ADVANCE);
            }
            return true;
        } finally {
            exit_section_(b, m, DO_LOOP, true);
        }

    }

}
