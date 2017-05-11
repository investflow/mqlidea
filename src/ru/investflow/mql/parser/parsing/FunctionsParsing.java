package ru.investflow.mql.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.parser.parsing.util.ParsingScope;
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
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.UNEXPECTED_TOKEN;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.hasElementInRange;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.matchSequenceN;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;
import static ru.investflow.mql.parser.parsing.util.TokenAdvanceMode.ADVANCE;

public class FunctionsParsing implements MQL4Elements {

    public enum FunctionParsingResult {
        NotMatched,
        Declaration,
        Definition
    }

    private static final TokenSet FUNCTION_DESCRIPTION_TOKENS = TokenSet.create(MQL4Elements.VIRTUAL_KEYWORD, MQL4Elements.CONST_KEYWORD);

    private static final List<PatternMatcher> FUNCTION_MATCHER = Arrays.asList(
            (b, ahead) -> { // return type: custom type or data type
                IElementType t = b.lookAhead(ahead);
                int pos = ParsingUtils.countLookAhead(b, 0, FUNCTION_DESCRIPTION_TOKENS);
                boolean ok = t == IDENTIFIER || MQL4TokenSets.DATA_TYPES.contains(t);
                if (!ok) {
                    return -1;
                }
                pos++; // 'type'
                pos += ParsingUtils.countLookAhead(b, pos, FUNCTION_DESCRIPTION_TOKENS);
                return pos;
            },
            (b, ahead) -> { // name or ClassName::name
                ParsingScope scope = ParsingScope.getScope(b);
                int pos = 0;
                if (scope == ParsingScope.TOP_LEVEL && b.lookAhead(ahead) == IDENTIFIER && b.lookAhead(ahead + 1) == COLON_COLON) {
                    pos += 2; // 'ClassName' + '::'
                }
                return b.lookAhead(ahead + pos) != IDENTIFIER ? -1 : pos + 1; // function name
            },
            (b, ahead) -> b.lookAhead(ahead) == L_ROUND_BRACKET ? 1 : -1 // opening bracket
    );

    private static final List<PatternMatcher> CONSTRUCTOR_DESTRUCTOR_MATCHER = Arrays.asList(
            (b, ahead) -> { // ClassName or ClassName::ClassName
                ParsingScope scope = ParsingScope.getScope(b);
                if (scope == ParsingScope.CODE_BLOCK) {
                    return -1;
                }
                int pos = 0;
                if (scope == ParsingScope.CLASS) {
                    if (b.lookAhead(ahead) == TILDA) { // '~'
                        pos++; // '~'
                    }
                    return b.lookAhead(ahead + pos) == IDENTIFIER ? pos + 1 : -1;
                }
                assert scope == ParsingScope.TOP_LEVEL;
                if (b.lookAhead(ahead) != IDENTIFIER) {
                    return -1;
                }
                pos++; // class name
                if (b.lookAhead(ahead + pos) != COLON_COLON) {
                    return -1;
                }
                pos++; // '::'
                if (b.lookAhead(ahead + pos) == TILDA) {
                    pos++; // '~'
                }
                return b.lookAhead(ahead + pos) == IDENTIFIER ? pos + 1 : -1; // method name
            },
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
        int matchedPrefixLen = matchSequenceN(b, FUNCTION_MATCHER, 0);
        if (matchedPrefixLen < 0) {
            matchedPrefixLen = matchSequenceN(b, CONSTRUCTOR_DESTRUCTOR_MATCHER, 0);
            if (matchedPrefixLen < 0) {
                return NotMatched;
            }
        }
        ParsingScope scope = ParsingScope.getScope(b);
        // Note: FUNCTION_MATCHER can also match constructors in case if constructor has 'void' prefix
        boolean canBeConstructorDefinition = (scope == ParsingScope.CLASS || hasElementInRange(b, matchedPrefixLen, MQL4Elements.COLON_COLON))
                && !hasElementInRange(b, matchedPrefixLen, MQL4Elements.TILDA);

        PsiBuilder.Marker m = b.mark();
        FunctionParsingResult actualResult = null;
        try {
            ParsingUtils.advanceLexer(b, matchedPrefixLen); // data type, identifier(::identifier), left round brace.

            boolean argsParsed = parseFunctionArgs(b, l);
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

            boolean hasFieldsInitBlock = false;
            if (b.getTokenType() == COLON && canBeConstructorDefinition) { // constructor in-place init
                b.advanceLexer(); // ':'
                if (!parseConstructorFieldsInitBlock(b, l)) {
                    advanceLexerUntil(b, ON_ERROR_STOP_TOKENS, ADVANCE);
                    return Definition;
                }
                hasFieldsInitBlock = true;
            } else if (b.getTokenType() == EQ) { // pure virtual function
                b.advanceLexer(); // '='
                String tt = b.getTokenText();
                if ("0".equals(tt) || "NULL".equals(tt)) {
                    b.advanceLexer(); // '=0'
                } else {
                    String err = "0 or NULL is expected";
                    if (b.getTokenType() != L_CURLY_BRACKET && b.getTokenType() != SEMICOLON) {
                        ParsingUtils.advanceWithError(b, err);
                    } else {
                        b.error(err);
                    }
                }
            }

            if (!hasFieldsInitBlock && b.getTokenType() == SEMICOLON) {
                actualResult = Declaration;
            } else if (b.getTokenType() == L_CURLY_BRACKET) {
                actualResult = Definition;
                parseBracketsBlock(b, l);
            } else {
                if (!hasFieldsInitBlock && expectedResult == Declaration) {
                    b.error("Semicolon expected");
                } else if (hasFieldsInitBlock || expectedResult == Definition) {
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

    //todo: naming convention: parse || force? report error or not?
    private static boolean parseConstructorFieldsInitBlock(PsiBuilder b, int l) {
        PsiBuilder.Marker m = b.mark();
        try {
            while (b.getTokenType() != L_CURLY_BRACKET) {
                // field name
                if (!ClassParsing.parseCustomTypeName(b, l)) {
                    b.error(ParsingErrors.IDENTIFIER_EXPECTED);
                    return false;
                }
                if (!BracketBlockParsing.parseBracketsBlock(b, l, true)) {
                    ParsingUtils.advanceWithError(b, UNEXPECTED_TOKEN);
                    return false;
                }
                if (b.getTokenType() == COMMA) {
                    b.advanceLexer(); // ','
                    continue; // end of field
                }
                if (b.getTokenType() == CONST_KEYWORD) {
                    b.advanceLexer(); // 'const'
                }
                if (b.getTokenType() == L_CURLY_BRACKET) {
                    break;
                }
                b.error(UNEXPECTED_TOKEN);
                return false;
            }
            return true;
        } finally {
            m.done(CLASS_INIT_BLOCK);
        }
    }

    /**
     * Form: (TYPE [IDENTIFIER [= (literal|identifier)]])
     */
    @SuppressWarnings("ConstantConditions")
    private static boolean parseFunctionArgs(PsiBuilder b, int l) {
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
                    boolean customType = ClassParsing.parseCustomTypeName(b, l);
                    if (!customType) {
                        if (!MQL4TokenSets.DATA_TYPES.contains(t1)) { // not custom type name or known type
                            b.error(UNEXPECTED_TOKEN);
                            return false;
                        }
                        b.advanceLexer(); // type
                    }

                    // === Second element ===
                    IElementType t2 = b.getTokenType();
                    if (t2 == CONST_KEYWORD) {
                        if (hasConst) {
                            b.error(UNEXPECTED_TOKEN);
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
                        b.error(UNEXPECTED_TOKEN);
                        return false;
                    }
                    b.advanceLexer(); // EQ

                    // default arg value
                    if (!ExpressionParsing.parseCompileTimeEvalExpression(b, l, false, ExpressionParsing.COMPILE_TIME_VALUE, R_ROUND_BRACKET)) {
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
                    b.error(UNEXPECTED_TOKEN);
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
