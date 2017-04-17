package ru.investflow.mql.parser.parsing.function;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.containers.Predicate;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.BracketBlockParsing.parseBracketsBlock;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.Declaration;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.Definition;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.matchSequence;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.parseTokenOrFail;
import static ru.investflow.mql.parser.parsing.util.TokenAdvanceMode.ADVANCE;

public class FunctionsParsing implements MQL4Elements {

    public enum FunctionParsingResult {
        Declaration,
        Definition
    }

    private static final List<Predicate<IElementType>> FUNCTION_START_MATCHER = Arrays.asList(
            MQL4TokenSets.DATA_TYPES::contains,
            t -> t == IDENTIFIER,
            t -> t == L_ROUND_BRACKET
    );

    public static final TokenSet STOP_TOKENS = TokenSet.create(SEMICOLON, R_ROUND_BRACKET);

    public static boolean parseFunction(PsiBuilder b) {
        return parseFunction(b, 0, null) != null;
    }

    /**
     * Form: TYPE IDENTIFIER ( ARG* ) (; |  {})
     */
    @Nullable
    public static FunctionParsingResult parseFunction(PsiBuilder b, int l, @Nullable FunctionParsingResult expectedResult) {
        if (!recursion_guard_(b, l, "parseFunction")) {
            return null;
        }
        if (!matchSequence(b, FUNCTION_START_MATCHER)) {
            return null;
        }

        PsiBuilder.Marker m = b.mark();
        FunctionParsingResult actualResult = null;
        try {
            b.advanceLexer(); // data type
            b.advanceLexer(); // identifier
            b.advanceLexer(); // '('

            boolean argsParsed = parseFunctionArgs(b, l + 1);
            if (!argsParsed) {
                ParsingUtils.advanceLexerUntil(b, STOP_TOKENS, ADVANCE);
                return expectedResult;
            }

            if (!parseTokenOrFail(b, R_ROUND_BRACKET)) { // ')'
                ParsingUtils.advanceLexerUntil(b, STOP_TOKENS, ADVANCE);
                return expectedResult;
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

    private static boolean parseFunctionArgs(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseFunctionArgs")) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        try {
            while (b.getTokenType() != R_ROUND_BRACKET) {
                PsiBuilder.Marker m2 = enter_section_(b);
                try {
                    if (!MQL4TokenSets.DATA_TYPES.contains(b.getTokenType())) {
                        b.error("Argument type or right brace is expected");
                        return false;
                    }
                    b.advanceLexer();
                    if (b.getTokenType() != IDENTIFIER) {
                        b.error("Identifier expected");
                        return false;
                    }
                    b.advanceLexer();
                    if (b.getTokenType() != R_ROUND_BRACKET && b.getTokenType() != COMMA) {
                        b.error("Comma expected");
                        return false;
                    }
                    if (b.getTokenType() == COMMA) {
                        b.advanceLexer();
                    }
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
