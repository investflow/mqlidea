package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.parsing.util.ParsingUtils;
import ru.investflow.mql.psi.MQL4Elements;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.ExpressionParsing.parseExpressionOrFail;
import static ru.investflow.mql.parser.parsing.LiteralParsing.isLiteral;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.assertNoLineBreaksInRange;

public class PreprocessorIfDefParsing implements MQL4Elements {
    // #define identifier expression                   // parameter-free form
    // #define identifier(par1,... par8) expression    // parametric form
    //TODO: line breaks?

    public static boolean parseDefine(PsiBuilder b, int l) {
        if (!ParsingUtils.nextTokenIs(b, l, "parseDefine", DEFINE_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        b.advanceLexer(); // #define
        try {
            if (!parseRequiredIdentifier(b)) {
                return true;
            }
            parseDefineParams(b);
            parseExpressionOrFail(b, l + 1);
        } finally {
            m.done(PREPROCESSOR_DEFINE_BLOCK);
        }
        return true;
    }


    public static boolean parseUndef(PsiBuilder b, int l) {
        if (!ParsingUtils.nextTokenIs(b, l, "parseUndef", UNDEF_KEYWORD)) {
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
            exit_section_(b, m, PREPROCESSOR_UNDEF_BLOCK, true);
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
            //error(b, "Identifier expected");
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
