package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import static ru.investflow.mql.parser.parsing.preprocessor.PreprocessorParsing.completePPLineStatement;
import static ru.investflow.mql.parser.parsing.util.ParsingErrors.error;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceLexer;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.containsEndOfLineOrFile;

public class PreprocessorPropertyParsing implements MQL4Elements {

    public static boolean parseProperty(PsiBuilder b) {
        if (b.getTokenType() != PROPERTY_PP_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        int offset = b.getCurrentOffset();
        b.advanceLexer(); // #property -> name
        try {
            if (containsEndOfLineOrFile(b, offset)) { // new line or EOF after #property -> report error
                error(b, ParsingErrors.IDENTIFIER_EXPECTED);
                return true;
            }
            offset = b.getCurrentOffset();
            if (b.getTokenType() != IDENTIFIER) { // #property name is not identifier -> report error
                completePPLineStatement(b, offset, ParsingErrors.IDENTIFIER_EXPECTED);
                return true;
            }
            b.advanceLexer(); // name -> value
            if (containsEndOfLineOrFile(b, offset)) { // line ends after identifier -> end of block
                return true;
            }
            offset = b.getCurrentOffset();
            IElementType valueType = b.getTokenType();
            if (valueType == MINUS) {
                valueType = advanceLexer(b); // minus
            }
            if (!(valueType == IDENTIFIER || MQL4TokenSets.LITERALS.contains(valueType))) {
                completePPLineStatement(b, offset, "Illegal #property value");
                return true;
            }
            b.advanceLexer(); // name -> next token
            completePPLineStatement(b, offset);
            return true;
        } finally {
            m.done(PREPROCESSOR_PROPERTY_BLOCK);
        }
    }

}
