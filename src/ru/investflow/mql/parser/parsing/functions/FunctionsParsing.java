package ru.investflow.mql.parser.parsing.functions;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.TokenSet;
import ru.investflow.mql.parser.parsing.CodeBlockParsing;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.functions.FunctionsParsing.FunctionParsingResult.Declaration;
import static ru.investflow.mql.parser.parsing.functions.FunctionsParsing.FunctionParsingResult.Definition;
import static ru.investflow.mql.parser.parsing.functions.FunctionsParsing.FunctionParsingResult.Failed;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.StopTokenAdvanceMode.ADVANCE_STOP_TOKENS;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.matchSequence;

public class FunctionsParsing implements MQL4Tokens {
    public enum FunctionParsingResult {
        Failed,
        Declaration,
        Definition
    }

    public static final TokenSet STOP_TOKENS = TokenSet.create(SEMICOLON, RPARENTH);

    /**
     * Form: TYPE IDENTIFIER ( ARG* ) (; |  {})
     */
    @NotNull
    public static FunctionParsingResult parseFunction(PsiBuilder b, int l, @Nullable FunctionParsingResult expectedResult) {
        if (!recursion_guard_(b, l, "parseFunction")) {
            return Failed;
        }
        if (!matchSequence(b, Arrays.asList(
                MQL4TokenSets.DATA_TYPES::contains,
                t -> t == IDENTIFIER,
                t -> t == LPARENTH
        ))) {
            return Failed;
        }
        FunctionParsingResult result = expectedResult == null ? Definition : expectedResult;
        PsiBuilder.Marker m = enter_section_(b);
        try {
            b.advanceLexer(); // data type
            b.advanceLexer(); // identifier
            b.advanceLexer(); // '('

            boolean argsParsed = parseArgumentsList(b, l + 1);
            if (!argsParsed) {
                ParsingUtils.advanceLexerUntil(b, STOP_TOKENS, ADVANCE_STOP_TOKENS);
                return result;
            }
            if (b.getTokenType() != RPARENTH) {
                b.error("Right brace expected");
                ParsingUtils.advanceLexerUntil(b, STOP_TOKENS, ADVANCE_STOP_TOKENS);
                return result;
            }
            b.advanceLexer(); // ')'

            if (b.getTokenType() == SEMICOLON) {
                b.advanceLexer();
                result = Declaration;
            } else if (b.getTokenType() == LBRACE) {
                CodeBlockParsing.parseBlock(b, l + 1);
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
            exit_section_(b, m, result == Declaration ? MQL4Elements.FUNCTION_DECLARATION : MQL4Elements.FUNCTION_DEFINITION, true);
        }
        return result;
    }

    private static boolean parseArgumentsList(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseArgumentsList")) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        try {
            while (b.getTokenType() != RPARENTH) {
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
                    if (b.getTokenType() != RPARENTH && b.getTokenType() != COMMA) {
                        b.error("Comma expected");
                        return false;
                    }
                    if (b.getTokenType() == COMMA) {
                        b.advanceLexer();
                    }
                } finally {
                    exit_section_(b, m2, MQL4Elements.ARGUMENT, true);
                }
            }
        } finally {
            exit_section_(b, m, MQL4Elements.ARGUMENTS_LIST, true);
        }
        return true;
    }
}
