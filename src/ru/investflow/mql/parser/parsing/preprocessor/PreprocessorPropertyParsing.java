package ru.investflow.mql.parser.parsing.preprocessor;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.hash.HashMap;
import ru.investflow.mql.psi.MQL4TokenTypes;

import static com.intellij.lang.java.parser.JavaParserUtil.error;
import static com.intellij.lang.parser.GeneratedParserUtilBase.enter_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.exit_section_;
import static com.intellij.lang.parser.GeneratedParserUtilBase.nextTokenIs;
import static com.intellij.lang.parser.GeneratedParserUtilBase.recursion_guard_;
import static ru.investflow.mql.parser.parsing.LiteralParsing.advanceIfLiteral;
import static ru.investflow.mql.psi.MQL4TokenTypeSets.LITERALS;
import static ru.investflow.mql.psi.MQL4TokenTypes.DOUBLE_LITERAL;
import static ru.investflow.mql.psi.MQL4TokenTypes.INTEGER_LITERAL;
import static ru.investflow.mql.psi.MQL4TokenTypes.PREPROCESSOR_PROPERTY_BLOCK;
import static ru.investflow.mql.psi.MQL4TokenTypes.PROPERTY_KEYWORD;
import static ru.investflow.mql.psi.MQL4TokenTypes.STRING_LITERAL;

public class PreprocessorPropertyParsing {

    public static boolean parsePropertyBlock(PsiBuilder b, int l) {
        if (!recursion_guard_(b, l, "PreprocessorPropertyBlock")) {
            return false;
        }
        if (!nextTokenIs(b, PROPERTY_KEYWORD)) {
            return false;
        }
        PsiBuilder.Marker m = enter_section_(b);
        b.advanceLexer(); // #property
        try {
            IElementType id = b.getTokenType();
            if (id != MQL4TokenTypes.IDENTIFIER) {
                error(b, "Identifier expected");
                return true;
            }
            String propertyName = b.getTokenText();
            PropertyValueValidator valueValidator = VALIDATORS_BY_NAME.get(propertyName);
            if (valueValidator == null) {
                b.error("Unknown property");
            }
            b.advanceLexer();
            if (valueValidator != null) {
                valueValidator.validateValue(b);
            }
            advanceIfLiteral(b);
        } finally {
            exit_section_(b, m, PREPROCESSOR_PROPERTY_BLOCK, true);
        }
        return true;
    }


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
        VALIDATORS_BY_NAME.put("indicator_minimum", new RequiredLiteralValidator(DOUBLE_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_maximum", new RequiredLiteralValidator(DOUBLE_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_labelN", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_colorN", new RequiredLiteralValidator(INTEGER_LITERAL)); //todo: color
        VALIDATORS_BY_NAME.put("indicator_widthN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_styleN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_typeN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_levelN", new RequiredLiteralValidator(DOUBLE_LITERAL));
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
                b.error("Literal expected!");
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
