package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.nextTokenIs;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.FunctionParsingResult.Declaration;
import static ru.investflow.mql.parser.parsing.function.FunctionsParsing.parseFunction;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;

public class PreprocessorImportParsing implements MQL4Tokens {

    public static boolean parseImport(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseImport")) {
            return false;
        }
        if (!nextTokenIs(b, IMPORT_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        int importOffset = b.getCurrentOffset();
        b.advanceLexer();
        try {
            if (ParsingUtils.containsEndOfLine(b, importOffset)) { // block is finished. Nothing to parse.
                return true;
            }
            // the only parameter must be string literal
            int literalOffset = b.getCurrentOffset();
            IElementType tt = b.getTokenType();
            if (tt != STRING_LITERAL) {
                b.error("String literal is expected!");
                ParsingUtils.advanceLexerUntil(b, LINE_TERMINATOR);
                return true;
            }
            b.advanceLexer();

            // check that there are no other tokens till the eol
            if (!ParsingUtils.containsEndOfLine(b, literalOffset)) {
                b.error("New line is expected!");
                ParsingUtils.advanceLexerUntil(b, LINE_TERMINATOR);
                return true;
            }
            // now parse function declaration until the next import block
            while (!nextTokenIs(b, IMPORT_KEYWORD)) {
                forceParseDeclaration(b, l + 1);
            }
        } finally {
            exit_section_(b, m, MQL4Elements.PREPROCESSOR_IMPORT_BLOCK, true);
        }
        return true;
    }

    public static boolean forceParseDeclaration(PsiBuilder b, int l) {
        if (parseFunction(b, l, Declaration) == Declaration) {
            return true;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.error("Function declaration expected!");
        advanceLexerUntil(b, SEMICOLON);
        exit_section_(b, m, MQL4Elements.FUNCTION_DECLARATION, true);
        return false;
    }

}
