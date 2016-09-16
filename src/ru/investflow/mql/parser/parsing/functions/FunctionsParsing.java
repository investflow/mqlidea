package ru.investflow.mql.parser.parsing.functions;

import java.util.Arrays;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.matchSequence;

public class FunctionsParsing implements MQL4Tokens {

    public static boolean forceParseDeclaration(PsiBuilder b, int l) {
        if (parseDeclaration(b, l)) {
            return true;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.error("Function declaration expected!");
        advanceLexerUntil(b, SEMICOLON);
        exit_section_(b, m, MQL4Elements.FUNCTION_DECLARATION_BLOCK, true);
        return false;
    }

    /**
     * Form: TYPE IDENTIFIER ( ARG* ) ;
     */
    public static boolean parseDeclaration(PsiBuilder b, int l) {
        if (!matchSequence(b, Arrays.asList(
                MQL4TokenSets.DATA_TYPES::contains,
                t -> t == IDENTIFIER,
                t -> t == LBRACE,
                t -> true, //TODO: isArg
                t -> t == RBRACE
        ))) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer(); // data type
        b.advanceLexer(); // identifier
        b.advanceLexer(); // lbrace
        b.advanceLexer(); // TODO: parse arg
        b.advanceLexer(); // rbrace
        if (b.getTokenType() != SEMICOLON) {
            b.error("Semicolon expected");
        } else {
            b.advanceLexer();
        }
        exit_section_(b, m, MQL4Elements.FUNCTION_DECLARATION_BLOCK, true);
        return true;
    }
}
