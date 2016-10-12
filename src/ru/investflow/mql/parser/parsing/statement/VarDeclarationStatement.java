package ru.investflow.mql.parser.parsing.statement;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.ExpressionParsing.parseExpressionOrFail;

public class VarDeclarationStatement implements MQL4Tokens {

    private static final TokenSet PRE_TYPES = TokenSet.create(CONST_KEYWORD, EXTERN_KEYWORD, INPUT_KEYWORD, STATIC_KEYWORD);

    public static boolean parseVarDeclaration(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseVarDeclaration")) {
            return false;
        }
        int n = 0;
        if (PRE_TYPES.contains(b.lookAhead(n))) {
            n++;
        }
        boolean firstIsType = MQL4TokenSets.DATA_TYPES.contains(b.lookAhead(n));
        if (!(firstIsType && b.lookAhead(n + 1) == IDENTIFIER)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        if (n == 1) {
            b.advanceLexer(); // pre-type
        }
        b.advanceLexer(); // type
        boolean ok = parseVarDefinitionList(b, l + 1);
        if (!ok) {
            ParsingUtils.advanceLexerUntil(b, SEMICOLON);
        }
        exit_section_(b, m, MQL4Elements.VAR_DECLARATION_STATEMENT, true);
        return true;
    }

    private static boolean parseVarDefinitionList(PsiBuilder b, int l) {
        assert b.getTokenType() == IDENTIFIER;
        boolean ok = true;
        PsiBuilder.Marker m0 = enter_section_(b);
        do {
            PsiBuilder.Marker m1 = enter_section_(b);
            b.advanceLexer();
            try {
                if (b.getTokenType() == SEMICOLON) {
                    break;
                }
                if (b.getTokenType() == COMMA) {
                    continue;
                }
                if (b.getTokenType() == EQ) {
                    b.advanceLexer();
                    ok = parseExpressionOrFail(b, l + 1);
                }
            } finally {
                exit_section_(b, m1, MQL4Elements.VAR_DEFINITION, true);
            }
        } while (ok && b.getTokenType() == IDENTIFIER);
        exit_section_(b, m0, MQL4Elements.VAR_DEFINITION_LIST, true);
        return ok;
    }
}
