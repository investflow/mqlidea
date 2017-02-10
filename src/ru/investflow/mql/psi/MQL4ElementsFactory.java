package ru.investflow.mql.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import ru.investflow.mql.psi.impl.MQL4PreprocessorIncludeBlock;
import ru.investflow.mql.psi.impl.MQL4PreprocessorPropertyBlock;
import ru.investflow.mql.psi.impl.MQL4PsiElement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MQL4ElementsFactory implements MQL4Elements {

    private final static Map<ASTNode, Function<ASTNode, PsiElement>> elementFactory = new HashMap<>();

    public static PsiElement createElement(@NotNull ASTNode node) {
        return elementFactory.computeIfAbsent(node, n -> {
            IElementType type = n.getElementType();
            if (type == PREPROCESSOR_PROPERTY_BLOCK) {
                return MQL4PreprocessorPropertyBlock::new;
            }
            if (type == PREPROCESSOR_INCLUDE_BLOCK) {
                return MQL4PreprocessorIncludeBlock::new;
            }
            return MQL4PsiElement::new;
        }).apply(node);
    }
}
