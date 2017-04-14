package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.completePPStatement;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.containsEndOfLineOrFile;

public class PreprocessorPropertyParsing implements MQL4Elements {

    public static boolean parseProperty(PsiBuilder b) {
        if (b.getTokenType() != PROPERTY_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        int offset = b.getCurrentOffset();
        b.advanceLexer(); // #property -> name
        try {
            if (containsEndOfLineOrFile(b, offset)) { // new line or EOF after #property -> report error
                b.error(ParsingErrors.IDENTIFIER_EXPECTED);
                return true;
            }
            offset = b.getCurrentOffset();
            if (b.getTokenType() != IDENTIFIER) { // #property name is not identifier -> report error
                completePPStatement(b, offset, ParsingErrors.IDENTIFIER_EXPECTED);
                return true;
            }
            b.advanceLexer(); // name -> value
            if (containsEndOfLineOrFile(b, offset)) { // line ends after identifier -> end of block
                return true;
            }
            offset = b.getCurrentOffset();
            if (!MQL4TokenSets.LITERALS.contains(b.getTokenType())) {
                completePPStatement(b, offset, "Illegal #property value");
                return true;
            }
            b.advanceLexer(); // name -> next token
            completePPStatement(b, offset);
            return true;
        } finally {
            m.done(PREPROCESSOR_PROPERTY_BLOCK);
        }
    }

}
