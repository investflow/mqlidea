package ru.investflow.mql.parser.parsing.functions;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;

public class FunctionsParsing {
    public static boolean forceParseDeclaration(PsiBuilder b, int l) {
        if (parseDeclaration(b, l)) {
            return true;
        }

        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer();
        b.error("Function declaration expected!");
        exit_section_(b, m, MQL4Elements.FUNCTION_DECLARATION_BLOCK, true);

        return false;
    }

    public static boolean parseDeclaration(PsiBuilder b, int l) {
        //todo:
        b.advanceLexer();
        return true;
    }
}
