package ru.investflow.mql.parser.parsing.preprocessor;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.hash.HashMap;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.parser.parsing.util.ParsingErrors;
import ru.investflow.mql.psi.MQL4Elements;

import java.util.Map;

import static ru.investflow.mql.parser.parsing.LiteralParsing.isLiteral;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceUntilNewLine;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.advanceWithError;
import static ru.investflow.mql.parser.parsing.util.ParsingUtils.containsEndOfLine;
import static ru.investflow.mql.psi.MQL4TokenSets.LITERALS;

public class PreprocessorPropertyParsing implements MQL4Elements {

    public static boolean parseProperty(PsiBuilder b) {
        if (b.getTokenType() != PROPERTY_KEYWORD) {
            return false;
        }
        PsiBuilder.Marker m = b.mark();
        int offset = b.getCurrentOffset();
        b.advanceLexer(); // #property -> name
        try {
            if (containsEndOfLine(b, offset)) { // new line after #property -> report error
                b.error(ParsingErrors.IDENTIFIER_EXPECTED);
                return true;
            }
            offset = b.getCurrentOffset();
            if (b.getTokenType() != IDENTIFIER) { // #property name is not identifier -> report error
                advanceWithErrorUntilNewLine(b, offset, ParsingErrors.IDENTIFIER_EXPECTED);
                return true;
            }
            b.advanceLexer(); // name -> value
            if (containsEndOfLine(b, offset)) { // line ends after identifier -> end of block
                return true;
            }
            offset = b.getCurrentOffset();
            if (!isLiteral(b)) {
                advanceWithErrorUntilNewLine(b, offset, "Illegal #property value");
                return true;
            }
            b.advanceLexer(); // name -> next token
            if (!containsEndOfLine(b, offset)) { // line ends after identifier -> end of block
                advanceWithErrorUntilNewLine(b, offset, ParsingErrors.UNEXPECTED_TOKEN);
            }
            return true;
        } finally {
            m.done(PREPROCESSOR_PROPERTY_BLOCK);
        }
    }

    private static void advanceWithErrorUntilNewLine(PsiBuilder b, int offset, String message) {
        advanceWithError(b, message);
        if (!containsEndOfLine(b, offset)) { // line ends after identifier -> end of block
            advanceUntilNewLine(b);
        }
    }


    //todo: move validators to inspection
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, PropertyValueValidator> VALIDATORS_BY_NAME = new HashMap<>();

    static {
        VALIDATORS_BY_NAME.put("strict", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("icon", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("link", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("copyright", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("version", new RequiredAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("description", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("stacksize", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("library", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("indicator_chart_window", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("indicator_separate_window", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("indicator_height", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_buffers", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_minimum", new RequiredNumericValidator());
        VALIDATORS_BY_NAME.put("indicator_maximum", new RequiredNumericValidator());
        VALIDATORS_BY_NAME.put("indicator_labelN", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_colorN", new RequiredLiteralValidator(INTEGER_LITERAL)); //todo: color
        VALIDATORS_BY_NAME.put("indicator_widthN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_styleN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_typeN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_levelN", new RequiredNumericValidator());
        VALIDATORS_BY_NAME.put("indicator_levelcolor", new RequiredLiteralValidator(INTEGER_LITERAL)); //todo: color
        VALIDATORS_BY_NAME.put("indicator_levelwidth", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_levelstyle", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("script_show_confirm", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("script_show_inputs", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("tester_file", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("script_show_inputs", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("tester_indicator", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("tester_library", new RequiredLiteralValidator(STRING_LITERAL));

    }

    private interface PropertyValueValidator {
        @SuppressWarnings("unused")
        void validateValue(PsiBuilder b);
    }

    public static class RequiredLiteralValidator implements PropertyValueValidator {
        @NotNull
        private final IElementType type;

        public RequiredLiteralValidator(@NotNull IElementType type) {
            this.type = type;
        }

        @Override
        public void validateValue(PsiBuilder b) {
            if (b.getTokenType() != type) {
                b.error(type + " is expected");
            }
        }
    }

    public static class RequiredAnyLiteralValidator implements PropertyValueValidator {
        @Override
        public void validateValue(PsiBuilder b) {
            if (!LITERALS.contains(b.getTokenType())) {
                b.error("Literal expected");
            }
        }
    }

    public static class RequiredNumericValidator implements PropertyValueValidator {
        @Override
        public void validateValue(PsiBuilder b) {
            IElementType t = b.getTokenType();
            if (t != INTEGER_LITERAL && t != DOUBLE_LITERAL) {
                b.error("Numeric literal expected");
            }
        }
    }

    public static class OptionalAnyLiteralValidator implements PropertyValueValidator {
        @Override
        public void validateValue(PsiBuilder b) {
            // do nothing
        }
    }
}
