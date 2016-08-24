// This is a generated file. Not intended for manual editing.
package ru.investflow.mql.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import ru.investflow.mql.psi.impl.MQL4CommentImpl;
import ru.investflow.mql.psi.impl.MQL4LiteralImpl;
import ru.investflow.mql.psi.impl.MQL4PreprocessorBlockImpl;
import ru.investflow.mql.psi.impl.MQL4PreprocessorPropertyBlockImpl;
import ru.investflow.mql.psi.impl.MQL4RootItemImpl;
import ru.investflow.mql.psi.impl.MQL4TopLevelDeclarationImpl;

public interface MQL4TokenTypes {

    IElementType WHITE_SPACE = new MQL4ElementType("WHITE_SPACE");
    IElementType LINE_TERMINATOR = new MQL4ElementType("LINE_TERMINATOR");
    IElementType RECOVERY_LINE_TERMINATOR = new MQL4ElementType("RECOVERY_LINE_TERMINATOR");

    IElementType INTEGER_LITERAL = new MQL4ElementType("INTEGER_LITERAL");
    IElementType DOUBLE_LITERAL = new MQL4ElementType("DOUBLE_LITERAL");
    IElementType STRING_LITERAL = new MQL4ElementType("STRING_LITERAL");
    IElementType CHAR_LITERAL = new MQL4ElementType("CHAR_LITERAL");

    IElementType LINE_COMMENT = new MQL4ElementType("LINE_COMMENT");
    IElementType BLOCK_COMMENT = new MQL4ElementType("BLOCK_COMMENT");

    IElementType PREPROCESSOR_BLOCK = new MQL4ElementType("PREPROCESSOR_BLOCK");
    IElementType PREPROCESSOR_PROPERTY_BLOCK = new MQL4ElementType("PREPROCESSOR_PROPERTY_BLOCK");
    IElementType ROOT_ITEM = new MQL4ElementType("ROOT_ITEM");
    IElementType TOP_LEVEL_DECLARATION = new MQL4ElementType("TOP_LEVEL_DECLARATION");

    IElementType IDENTIFIER = new MQL4TokenType("IDENTIFIER");

    IElementType PROPERTY_KEYWORD = new MQL4TokenType("PROPERTY_KEYWORD");

    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            if (type == LINE_COMMENT || type == BLOCK_COMMENT) {
                return new MQL4CommentImpl(node);
            } else if (type == INTEGER_LITERAL || type == CHAR_LITERAL || type == STRING_LITERAL || type == DOUBLE_LITERAL) {
                return new MQL4LiteralImpl(node);
            } else if (type == PREPROCESSOR_BLOCK) {
                return new MQL4PreprocessorBlockImpl(node);
            } else if (type == PREPROCESSOR_PROPERTY_BLOCK) {
                return new MQL4PreprocessorPropertyBlockImpl(node);
            } else if (type == ROOT_ITEM) {
                return new MQL4RootItemImpl(node);
            } else if (type == TOP_LEVEL_DECLARATION) {
                return new MQL4TopLevelDeclarationImpl(node);
            }
            throw new AssertionError("Unknown element type: " + type);
        }
    }
}
