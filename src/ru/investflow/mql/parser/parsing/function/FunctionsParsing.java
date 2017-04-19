package ru.investflow.mql.parser.parsing.function;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.Predicate;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.parsing.ExpressionParsing;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;
import ru.investflow.mql.util.ObjectUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.BracketBlockParsing.parseBracketsBlock;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.Declaration;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.Definition;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.NotMatched;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.matchSequence;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;
import static ru.investflow.mql.parser.parsing.util.TokenAdvanceMode.ADVANCE;

public class FunctionsParsing implements MQL4Elements {

    public enum FunctionParsingResult {
        NotMatched,
        Declaration,
        Definition
    }

    private static final List<Predicate<IElementType>> FUNCTION_START_MATCHER = Arrays.asList(
            MQL4TokenSets.DATA_TYPES::contains,
            t -> t == IDENTIFIER,
            t -> t == L_ROUND_BRACKET
    );

    public static final TokenSet ON_ERROR_STOP_TOKENS = TokenSet.create(SEMICOLON, R_ROUND_BRACKET, LINE_TERMINATOR);

    public static boolean parseFunction(PsiBuilder b) {
        return parseFunction(b, 0, null) != NotMatched;
    }

    /**
     * Form: TYPE IDENTIFIER ( ARG* ) (; |  {})
     */
    @NotNull
    public static FunctionParsingResult parseFunction(PsiBuilder b, int l, @Nullable FunctionParsingResult expectedResult) {
        assert expectedResult != NotMatched;
        if (!recursion_guard_(b, l, "parseFunction")) {
            return NotMatched;
        }
        if (!matchSequence(b, FUNCTION_START_MATCHER)) {
            return NotMatched;
        }

        PsiBuilder.Marker m = b.mark();
        FunctionParsingResult actualResult = null;
        try {
            b.advanceLexer(); // data type
            b.advanceLexer(); // identifier
            b.advanceLexer(); // '('

            boolean argsParsed = parseFunctionArgs(b, l + 1);
            if (!argsParsed) {
                advanceLexerUntil(b, ON_ERROR_STOP_TOKENS, ADVANCE);
                return ObjectUtils.firstNonNull(expectedResult, Declaration);
            }

            if (!parseTokenOrFail(b, R_ROUND_BRACKET)) { // ')'
                advanceLexerUntil(b, ON_ERROR_STOP_TOKENS, ADVANCE);
                return ObjectUtils.firstNonNull(expectedResult, Declaration);
            }

            if (b.getTokenType() == SEMICOLON) {
                actualResult = Declaration;
                b.advanceLexer();
            } else if (b.getTokenType() == L_CURLY_BRACKET) {
                actualResult = Definition;
                parseBracketsBlock(b, l + 1);
            } else {
                if (expectedResult == Declaration) {
                    b.error("Semicolon expected");
                } else if (expectedResult == Definition) {
                    b.error("Function body expected");
                } else {
                    b.error("Function body or semicolon expected");
                }
            }
        } finally {
            m.done(actualResult == Declaration ? FUNCTION_DECLARATION : FUNCTION_DEFINITION);
        }
        return actualResult;
    }

    /**
     * Form: (TYPE [IDENTIFIER [= (literal|identifier)]])
     */
    private static boolean parseFunctionArgs(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseFunctionArgs")) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        try {
            while (b.getTokenType() != R_ROUND_BRACKET) {
                PsiBuilder.Marker m2 = b.mark();
                try {
                    //  === First element ===
                    IElementType t1 = b.getTokenType();
                    if (!MQL4TokenSets.DATA_TYPES.contains(t1)) {
                        b.error(ParsingErrors.UNEXPECTED_TOKEN);
                        return false;
                    }
                    b.advanceLexer(); // type

                    // === Second element ===
                    IElementType t2 = b.getTokenType();
                    if (t2 == COMMA) {
                        b.advanceLexer(); // COMMA
                        continue; // end of argument
                    }
                    if (t2 == R_ROUND_BRACKET) {
                        break; // end of all parameters
                    }
                    if (t2 != IDENTIFIER) {
                        b.error(ParsingErrors.IDENTIFIER_EXPECTED);
                        return false;
                    }
                    b.advanceLexer(); // identifier

                    //  === Third element ===
                    IElementType t3 = b.getTokenType();
                    if (t3 == COMMA) {
                        b.advanceLexer(); // COMMA
                        continue; // end of argument
                    }
                    if (t3 == R_ROUND_BRACKET) {
                        break; // end of all parameters
                    }
                    if (t3 != EQ) { //
                        b.error(ParsingErrors.UNEXPECTED_TOKEN);
                        return false;
                    }
                    b.advanceLexer(); // EQ

                    // default arg value
                    if (!ExpressionParsing.parseExpression(b, l + 1)) {
                        return false;
                    }

                    //  === End of arg ===
                    IElementType t4 = b.getTokenType();
                    if (t4 == COMMA) {
                        b.advanceLexer(); // COMMA
                        continue; // end of argument
                    }
                    if (t4 == R_ROUND_BRACKET) {
                        break; // end of all parameters
                    }
                    b.error(ParsingErrors.UNEXPECTED_TOKEN);
                    return false;
                } finally {
                    m2.done(FUNCTION_ARG);
                }
            }
        } finally {
            m.done(FUNCTION_ARGS_LIST);
        }
        return true;
    }
}
