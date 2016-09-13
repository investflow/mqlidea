package ru.investflow.mql.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.impl.MQL4CommentImpl;
import ru.investflow.mql.psi.impl.MQL4LiteralImpl;
import ru.investflow.mql.psi.impl.MQL4PreprocessorBlockImpl;
import ru.investflow.mql.psi.impl.MQL4TopLevelDeclarationImpl;

public class MQL4ElementFactory implements MQL4Tokens, MQL4Elements {

    public static PsiElement createElement(ASTNode node) {
        IElementType type = node.getElementType();
        if (type == LINE_COMMENT || type == BLOCK_COMMENT) {
            return new MQL4CommentImpl(node);
        } else if (type == INTEGER_LITERAL || type == CHAR_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL || type == INCLUDE_STRING_LITERAL) {
            return new MQL4LiteralImpl(node);
        } else if (type == PREPROCESSOR_BLOCK) {
            return new MQL4PreprocessorBlockImpl(node);
        } else if (type == PREPROCESSOR_PROPERTY_BLOCK) {
            return new MQL4PreprocessorBlockImpl(node);
        } else if (type == PREPROCESSOR_DEFINE_BLOCK) {
            return new MQL4PreprocessorBlockImpl(node);
        } else if (type == PREPROCESSOR_UNDEF_BLOCK) {
            return new MQL4PreprocessorBlockImpl(node);
        } else if (type == PREPROCESSOR_INCLUDE_BLOCK) {
            return new MQL4PreprocessorBlockImpl(node);
        } else if (type == PREPROCESSOR_IMPORT_BLOCK) {
            return new MQL4PreprocessorBlockImpl(node);
        } else if (type == TOP_LEVEL_DECLARATION) {
            return new MQL4TopLevelDeclarationImpl(node);
        } else if (type == FUNCTION_DECLARATION_BLOCK) {
            return new MQL4TopLevelDeclarationImpl(node); //todo: customize
        }
        throw new AssertionError("Unknown element type: " + type);
    }
}
