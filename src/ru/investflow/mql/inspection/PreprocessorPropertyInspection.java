package ru.investflow.mql.inspection;

import com.intellij.codeInspection.CustomSuppressableInspectionTool;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.SuppressIntentionAction;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.SmartList;
import com.intellij.util.containers.hash.HashMap;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;
import ru.investflow.mql.psi.impl.MQL4PreprocessorPropertyBlock;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ru.investflow.mql.psi.MQL4Elements.COLOR_CONSTANT_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.COLOR_STRING_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.IDENTIFIER;
import static ru.investflow.mql.psi.MQL4Elements.INTEGER_LITERAL;
import static ru.investflow.mql.psi.MQL4Elements.STRING_LITERAL;

public class PreprocessorPropertyInspection extends LocalInspectionTool implements CustomSuppressableInspectionTool {

    private static final String UNKNOWN_PROPERTY_WARNING = "Unknown property";
    private static final String ARGUMENT_EXPECTED_WARNING = "Argument is expected";
    private static final String ILLEGAL_ARGUMENT_TYPE_WARNING = "Illegal argument type";
    private static final String NUMERIC_ARGUMENT_EXPECTED_WARNING = "Numeric argument is expected";

    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull final InspectionManager manager, final boolean isOnTheFly) {
        final PsiElement[] elements = file.getChildren();
        final List<ProblemDescriptor> descriptors = new SmartList<>();

        for (PsiElement e : elements) {
            ProgressManager.checkCanceled();
            if (e.getNode().getElementType() != MQL4Elements.PREPROCESSOR_PROPERTY_BLOCK) {
                continue;
            }
            MQL4PreprocessorPropertyBlock block = (MQL4PreprocessorPropertyBlock) e;
            block.sync();
            if (block.keyNode == null) {
                continue;
            }
            String name = block.keyNode.getText();
            String validatorName = getValidatorName(name);
            PropertyValueValidator validator = VALIDATORS_BY_NAME.get(validatorName);
            if (validator == null) {
                descriptors.add(manager.createProblemDescriptor(block.keyNode.getPsi(), block.keyNode.getPsi(), UNKNOWN_PROPERTY_WARNING, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true));
                continue;
            }
            ProblemDescriptor d = validator.validateAndReturnProblemDescriptor(manager, block.keyNode, block.valueNode);
            if (d != null) {
                descriptors.add(d);
            }
        }
        return descriptors.toArray(new ProblemDescriptor[0]);
    }

    @NotNull
    private static String getValidatorName(String name) {
        char lastChar = name.charAt(name.length() - 1);
        if (lastChar < '0' || lastChar > '9') {
            return name;
        }
        for (String cn : COUNTING_NAMES) {
            if (name.startsWith(cn) && NumberUtils.isDigits(name.substring(cn.length()))) {
                return cn + "N";
            }
        }
        return name;
    }

    @Nullable
    @Override
    public SuppressIntentionAction[] getSuppressActions(@Nullable PsiElement psiElement) {
        return null;
    }

    private static class RemovePropertyValueFix implements LocalQuickFix {

        @NotNull
        public String getName() {
            return "Remove value";
        }

        @NotNull
        public String getFamilyName() {
            return getName();
        }

        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement el = descriptor.getPsiElement();
            TextRange rangeToRemove = extendLeft(el, MQL4Elements.WHITE_SPACE);
            Document document = PsiDocumentManager.getInstance(project).getDocument(el.getContainingFile());
            assert document != null;
            document.deleteString(rangeToRemove.getStartOffset(), rangeToRemove.getEndOffset());

        }
    }

    @NotNull
    private static TextRange extendLeft(@NotNull PsiElement el, @NotNull IElementType siblingType) {
        int endOffset = el.getTextRange().getEndOffset();
        while (true) {
            PsiElement prev = el.getPrevSibling();
            if (prev == null || (prev.getNode().getElementType() != siblingType)) {
                break;
            }
            el = prev;
        }
        return new TextRange(el.getTextOffset(), endOffset);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public static final Map<String, PropertyValueValidator> VALIDATORS_BY_NAME = new HashMap<>();

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
        VALIDATORS_BY_NAME.put("indicator_plots", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_minimum", new RequiredNumericValidator());
        VALIDATORS_BY_NAME.put("indicator_maximum", new RequiredNumericValidator());
        VALIDATORS_BY_NAME.put("indicator_labelN", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_colorN", new RequiredLiteralValidator(INTEGER_LITERAL, COLOR_CONSTANT_LITERAL, COLOR_STRING_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_widthN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_styleN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_typeN", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_levelN", new RequiredNumericValidator());
        VALIDATORS_BY_NAME.put("indicator_levelcolor", new RequiredLiteralValidator(INTEGER_LITERAL, COLOR_CONSTANT_LITERAL, COLOR_STRING_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_levelwidth", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("indicator_levelstyle", new RequiredLiteralValidator(INTEGER_LITERAL));
        VALIDATORS_BY_NAME.put("script_show_confirm", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("script_show_inputs", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("tester_file", new OptionalAnyLiteralValidator());
        VALIDATORS_BY_NAME.put("script_show_inputs", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("tester_indicator", new RequiredLiteralValidator(STRING_LITERAL));
        VALIDATORS_BY_NAME.put("tester_library", new RequiredLiteralValidator(STRING_LITERAL));
    }

    public static final String[] COUNTING_NAMES = {"indicator_label", "indicator_color", "indicator_width", "indicator_style", "indicator_type", "indicator_level"};

    interface PropertyValueValidator {
        @Nullable
        ProblemDescriptor validateAndReturnProblemDescriptor(@NotNull InspectionManager manager, @NotNull ASTNode keyNode, @Nullable ASTNode valueNode);
    }

    static class RequiredLiteralValidator implements PropertyValueValidator {
        @NotNull
        private final IElementType[] types;

        public RequiredLiteralValidator(@NotNull IElementType... types) {
            this.types = types;
        }

        @Nullable
        @Override
        public ProblemDescriptor validateAndReturnProblemDescriptor(@NotNull InspectionManager manager, @NotNull ASTNode keyNode, @Nullable ASTNode valueNode) {
            if (valueNode == null) {
                return createMissedRequiredArgumentDescriptor(manager, keyNode);
            }
            IElementType valueType = valueNode.getElementType();
            // allow IDENTIFIER -> value can be a result of #define block.
            if (valueType != IDENTIFIER && Stream.of(types).noneMatch(t -> t == valueType)) {
                return manager.createProblemDescriptor(valueNode.getPsi(), valueNode.getPsi(), ILLEGAL_ARGUMENT_TYPE_WARNING, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true);
            }
            return null;
        }
    }

    static class RequiredAnyLiteralValidator implements PropertyValueValidator {
        @Nullable
        @Override
        public ProblemDescriptor validateAndReturnProblemDescriptor(@NotNull InspectionManager manager, @NotNull ASTNode keyNode, @Nullable ASTNode valueNode) {
            return valueNode == null ? createMissedRequiredArgumentDescriptor(manager, keyNode) : null;
        }
    }

    static class RequiredNumericValidator implements PropertyValueValidator {
        @Nullable
        @Override
        public ProblemDescriptor validateAndReturnProblemDescriptor(@NotNull InspectionManager manager, @NotNull ASTNode keyNode, @Nullable ASTNode valueNode) {
            if (valueNode == null) {
                return createMissedRequiredArgumentDescriptor(manager, keyNode);
            }
            if (!MQL4TokenSets.NUMBERS.contains(valueNode.getElementType())) {
                return manager.createProblemDescriptor(valueNode.getPsi(), valueNode.getPsi(), NUMERIC_ARGUMENT_EXPECTED_WARNING, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true);
            }
            return null;
        }
    }

    static class OptionalAnyLiteralValidator implements PropertyValueValidator {
        @Nullable
        @Override
        public ProblemDescriptor validateAndReturnProblemDescriptor(@NotNull InspectionManager manager, @NotNull ASTNode keyNode, @Nullable ASTNode valueNode) {
            return valueNode == null ? null : manager.createProblemDescriptor(valueNode.getPsi(), valueNode.getPsi(),
                    "No value is needed for '" + keyNode.getText() + "' property",
                    ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true, new RemovePropertyValueFix());
        }
    }

    @NotNull
    private static ProblemDescriptor createMissedRequiredArgumentDescriptor(@NotNull InspectionManager manager, @NotNull ASTNode keyNode) {
        return manager.createProblemDescriptor(keyNode.getPsi(), keyNode.getPsi(), ARGUMENT_EXPECTED_WARNING, ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true);
    }

}
