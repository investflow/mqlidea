package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.parser.parsing.util.PatternMatcher;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;
import ru.investflow.mql.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.BracketBlockParsing.parseBracketsBlock;
import static ru.investflow.mql.parser.parsing.FunctionsParsing.FunctionParsingResult.Declaration;
import static ru.investflow.mql.parser.parsing.FunctionsParsing.FunctionParsingResult.Definition;
import static ru.investflow.mql.parser.parsing.FunctionsParsing.FunctionParsingResult.NotMatched;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.matchSequenceN;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;
import static ru.investflow.mql.parser.parsing.util.TokenAdvanceMode.ADVANCE;

public class FunctionsParsing implements MQL4Elements {

    public enum FunctionParsingResult {
        NotMatched,
        Declaration,
        Definition
    }

    private static final List<PatternMatcher> FUNCTION_START_MATCHER = Arrays.asList(
            (b, ahead) -> { // return type: custom type or data type
                IElementType t = b.lookAhead(ahead);
                int da = 0;
                if (t == CONST_KEYWORD) {
                    da++;
                    t = b.lookAhead(ahead + da);
                }
                boolean ok = t == IDENTIFIER || MQL4TokenSets.DATA_TYPES.contains(t);
                if (ok) {
                    t = b.lookAhead(ahead + da + 1);
                    da += t == CONST_KEYWORD ? 1 : 0;
                }
                return ok ? 1 + da : -1;
            },
            (b, ahead) -> b.lookAhead(ahead) == IDENTIFIER ? 1 : -1,// function name
            (b, ahead) -> b.lookAhead(ahead) == L_ROUND_BRACKET ? 1 : -1 // opening bracket
    );

    public static final TokenSet ON_ERROR_STOP_TOKENS = TokenSet.create(SEMICOLON, R_ROUND_BRACKET, R_CURLY_BRACKET);

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
        int matchedPrefixLen = matchSequenceN(b, FUNCTION_START_MATCHER, 0);
        if (matchedPrefixLen < 0) {
            return NotMatched;
        }

        PsiBuilder.Marker m = b.mark();
        FunctionParsingResult actualResult = null;
        try {
            ParsingUtils.advanceLexer(b, matchedPrefixLen); // data type, identifier, left round brace.

            boolean argsParsed = parseFunctionArgs(b, l + 1);
            if (!argsParsed) {
                advanceLexerUntil(b, ON_ERROR_STOP_TOKENS, ADVANCE);
                return ObjectUtils.firstNonNull(expectedResult, Declaration);
            }

            if (!parseTokenOrFail(b, R_ROUND_BRACKET)) { // ')'
                advanceLexerUntil(b, ON_ERROR_STOP_TOKENS, ADVANCE);
                return ObjectUtils.firstNonNull(expectedResult, Declaration);
            }

            if (b.getTokenType() == CONST_KEYWORD) {
                b.advanceLexer(); // 'const'
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
                actualResult = Definition;
            }
        } finally {
            m.done(actualResult == Declaration ? FUNCTION_DECLARATION : FUNCTION_DEFINITION);
        }
        return actualResult;
    }

    /**
     * Form: (TYPE [IDENTIFIER [= (literal|identifier)]])
     */
    @SuppressWarnings("ConstantConditions")
    private static boolean parseFunctionArgs(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseFunctionArgs")) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        try {
            while (b.getTokenType() != R_ROUND_BRACKET) {
                PsiBuilder.Marker m2 = b.mark();
                boolean hasConst = false;
                try {
                    //  === First element ===
                    IElementType t1 = b.getTokenType();
                    if (t1 == CONST_KEYWORD) {
                        hasConst = true;
                        t1 = ParsingUtils.advanceLexer(b); // const
                    }
                    if (t1 != IDENTIFIER && !MQL4TokenSets.DATA_TYPES.contains(t1)) { // not custom type name or known type
                        b.error(ParsingErrors.UNEXPECTED_TOKEN);
                        return false;
                    }
                    b.advanceLexer(); // type

                    // === Second element ===
                    IElementType t2 = b.getTokenType();
                    if (t2 == CONST_KEYWORD) {
                        if (hasConst) {
                            b.error(ParsingErrors.UNEXPECTED_TOKEN);
                            return false;
                        }
                        b.advanceLexer(); // const
                        t2 = b.getTokenType();
                    }
                    if (t2 == AND || t2 == MUL) {
                        t2 = ParsingUtils.advanceLexer(b); // '&' || '*'
                    }
                    if (t2 == COMMA) {
                        b.advanceLexer(); // COMMA
                        continue; // end of argument
                    }
                    if (t2 == R_ROUND_BRACKET) {
                        break; // end of all parameters
                    }
                    if (t2 != IDENTIFIER && t2 != L_SQUARE_BRACKET) {
                        b.error(ParsingErrors.IDENTIFIER_EXPECTED);
                        return false;
                    }
                    if (t2 == IDENTIFIER) {
                        b.advanceLexer(); // identifier
                    }

                    if (b.getTokenType() == L_SQUARE_BRACKET) {
                        t2 = ParsingUtils.advanceLexer(b); // '['
                        if (t2 == INTEGER_LITERAL || t2 == IDENTIFIER) {
                            t2 = ParsingUtils.advanceLexer(b); // array size
                        }
                        if (t2 == R_SQUARE_BRACKET) {
                            b.advanceLexer(); //']'
                        } else {
                            b.error(ParsingErrors.NO_MATCHING_CLOSING_BRACKET);
                            return false;
                        }
                    }

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
                    if (!ExpressionParsing.parseCompileTimeEvalExpression(b, l + 1, false, ExpressionParsing.COMPILE_TIME_VALUE, R_ROUND_BRACKET)) {
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
