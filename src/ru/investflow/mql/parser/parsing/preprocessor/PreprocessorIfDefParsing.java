package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4Tokens;

import static com.intellij.lang.java.parser.JavaParserUtil.error;
import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.nextTokenIs;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.ExpressionParsing.parseExpression;
import static ru.investflow.mql.parser.parsing.LiteralParsing.isLiteral;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.assertNoLineBreaksInRange;

public class PreprocessorIfDefParsing implements MQL4Tokens {
    // #define identifier expression                   // parameter-free form
    // #define identifier(par1,... par8) expression    // parametric form
    //TODO: line breaks?

    public static boolean parseDefine(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseDefine")) {
            return false;
        }
        if (!nextTokenIs(b, DEFINE_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer(); // #define
        try {
            if (!parseRequiredIdentifier(b)) {
                return true;
            }

            parseDefineParams(b);

            boolean r = parseExpression(b, l + 1);
            if (!r) {
                b.error("Expression expected");
            }
        } finally {
            exit_section_(b, m, MQL4Elements.PREPROCESSOR_DEFINE_BLOCK, true);
        }
        return true;
    }


    public static boolean parseUndef(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseUndef")) {
            return false;
        }
        if (!nextTokenIs(b, UNDEF_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        int startOffset = b.getCurrentOffset();
        b.advanceLexer(); // #undef
        try {
            if (!assertNoLineBreaksInRange(b, startOffset, "Identifier expected")) {
                return true;
            }
            if (!parseRequiredIdentifier(b)) {
                return true;
            }
        } finally {
            exit_section_(b, m, MQL4Elements.PREPROCESSOR_UNDEF_BLOCK, true);
        }
        return true;
    }

    public static boolean parseIfDef(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "parseIfDef")) {
            return false;
        }
        //todo:
        return false;
    }

    private static boolean parseRequiredIdentifier(PsiBuilder b) {
        if (b.getTokenType() != IDENTIFIER) {
            error(b, "Identifier expected");
            if (isLiteral(b)) {
                b.advanceLexer();
            }
            return false;
        }
        b.advanceLexer();
        return true;
    }

    private static boolean parseDefineParams(PsiBuilder b) {
        //todo:
        return false;
    }

}
