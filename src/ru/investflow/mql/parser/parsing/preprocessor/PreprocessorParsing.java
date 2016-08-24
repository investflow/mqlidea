package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;

import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorPropertyParsing.parsePropertyBlock;
import static ru.investflow.mql.psi.MQL4TokenTypes.PREPROCESSOR_BLOCK;

public class PreprocessorParsing {

    public static boolean parsePreprocessorBlock(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PreprocessorBlock")) {
            return false;
        }

        PsiBuilder.Marker m = enter_section_(b);
        boolean r = parsePropertyBlock(b, l + 1);
        exit_section_(b, m, PREPROCESSOR_BLOCK, r);
        return r;
    }

}
