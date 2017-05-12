package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.parser.parsing.util.TokenAdvanceMode;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.nextTokenIs;
import static ru.investflow.mql.parser.parsing.FunctionsParsing.FunctionParsingResult.Declaration;
import static ru.investflow.mql.parser.parsing.FunctionsParsing.parseFunction;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.error;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexerUntil;
import static ru.investflow.mql.parser.parsing.util.TokenAdvanceMode.ADVANCE;

public class PreprocessorImportParsing implements MQL4Elements {

    public static boolean parseImport(PsiBuilder b, int l) {
        if (!ParsingUtils.nextTokenIs(b, l, "parseImport", IMPORT_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        int importOffset = b.getCurrentOffset();
        b.advanceLexer();
        try {
            if (ParsingUtils.containsEndOfLineOrFile(b, importOffset)) { // block is finished. Nothing to parse.
                return true;
            }
            // the only parameter must be string literal
            int literalOffset = b.getCurrentOffset();
            IElementType tt = b.getTokenType();
            if (tt != STRING_LITERAL) {
                error(b, "String literal is expected!");
                advanceLexerUntil(b, LINE_TERMINATOR, TokenAdvanceMode.DO_NOT_ADVANCE);
                return true;
            }
            b.advanceLexer();

            // check that there are no other tokens till the eol
            if (!ParsingUtils.containsEndOfLineOrFile(b, literalOffset)) {
                error(b, "New line is expected!");
                advanceLexerUntil(b, LINE_TERMINATOR, TokenAdvanceMode.DO_NOT_ADVANCE);
                return true;
            }
            // now parse function declaration until the next import block
            while (!nextTokenIs(b, IMPORT_KEYWORD)) {
                forceParseDeclaration(b, l + 1);
            }
        } finally {
            exit_section_(b, m, PREPROCESSOR_IMPORT_BLOCK, true);
        }
        return true;
    }

    public static boolean forceParseDeclaration(PsiBuilder b, int l) {
        if (parseFunction(b, l, Declaration) == Declaration) {
            return true;
        }
        PsiBuilder.Marker m = enter_section_(b);
        error(b, "Function declaration expected!");
        advanceLexerUntil(b, SEMICOLON, ADVANCE);
        exit_section_(b, m, FUNCTION_DECLARATION, true);
        return false;
    }

}
