package ru.investflow.mql.parser.parsing.statement;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.parser.parsing.util.TokenAdvanceMode;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.checkTokenOrFail;

public class VarDeclarationStatement implements MQL4Elements {

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
            ParsingUtils.advanceLexerUntil(b, SEMICOLON, TokenAdvanceMode.ADVANCE);
        }
        exit_section_(b, m, VAR_DECLARATION_STATEMENT, true);
        return true;
    }

    public static boolean parseVarDefinitionList(PsiBuilder b, int l) {
        assert b.getTokenType() == IDENTIFIER;
        PsiBuilder.Marker m0 = enter_section_(b);
        try {
            boolean ok = true;
            do {
                PsiBuilder.Marker m1 = enter_section_(b);
                try {
                    b.advanceLexer();
                    if (b.getTokenType() == SEMICOLON) {
                        b.advanceLexer();
                        break;
                    }
                    if (b.getTokenType() == COMMA) {
                        continue;
                    }
                    if (b.getTokenType() == EQ) {
                        b.advanceLexer();
                        ok = false;//TODO: parseExpressionOrFail(b, l + 1);
                        if (ok && b.getTokenType() == SEMICOLON) {
                            b.advanceLexer();
                            break;
                        }
                    } else {
                        b.error(ParsingErrors.UNEXPECTED_TOKEN);
                        ok = false;
                    }
                } finally {
                    exit_section_(b, m1, VAR_DEFINITION, true);
                }
            } while (ok && b.getTokenType() == IDENTIFIER);
            return ok;
        } finally {
            exit_section_(b, m0, VAR_DEFINITION_LIST, true);
        }
    }

    /**
     * Var declaration inside another block. Like 1 section if for loop. Does not allow empty vars or pre-types
     */
    public static boolean parseEmbeddedVarDeclarationOrAssignmentOrFail(PsiBuilder b, int l, @NotNull IElementType sectionType, @NotNull IElementType assignmentSectionType) {
        if (!recursion_guard_(b, l, "parseEmbeddedVarDeclarationOrAssignment")) {
            return false;
        }
        boolean firstIsType = MQL4TokenSets.DATA_TYPES.contains(b.getTokenType());
        PsiBuilder.Marker m = enter_section_(b);
        try {
            if (firstIsType) {
                b.advanceLexer();
            }
            //noinspection SimplifiableIfStatement
            if (firstIsType && !checkTokenOrFail(b, IDENTIFIER)) {
                return false;
            }
            return parseEmbeddedVarAssignmentsListOrFail(b, l + 1, assignmentSectionType, SEMICOLON);
        } finally {
            exit_section_(b, m, sectionType, true);
        }
    }

    public static boolean parseEmbeddedVarAssignmentsListOrFail(PsiBuilder b, int l, @NotNull IElementType sectionType, @NotNull IElementType stopToken) {
        if (!recursion_guard_(b, l, "parseEmbeddedVarAssignmentsList")) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        try {
            if (b.getTokenType() == stopToken) {
                return true;
            }
            while (true) {
                boolean ok = ParsingUtils.parseTokenOrFail(b, IDENTIFIER)
                        && ParsingUtils.parseTokenOrFail(b, EQ)
                        //TODO: && parseExpressionOrFail(b, l + 1)
                ;
                if (!ok) {
                    return false;
                }
                if (b.getTokenType() == stopToken) {
                    return true;
                }
                if (!ParsingUtils.parseTokenOrFail(b, COMMA)) {
                    return false;
                }
            }
        } finally {
            exit_section_(b, m, sectionType, true);
        }

    }

}
