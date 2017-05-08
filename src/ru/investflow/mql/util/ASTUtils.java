package ru.investflow.mql.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

public class ASTUtils {

    @Nullable
    public static ASTNode findLastPrevByType(ASTNode node, IElementType type) {
        ASTNode prev = node;
        while (prev != null) {
            prev = prev.getTreePrev();
            if (prev.getElementType() == type) {
                return prev;
            }
        }
        return null;
    }

    @Nullable
    public static ASTNode findLastChildByType(ASTNode node, IElementType type) {
        ASTNode child = node.getLastChildNode();
        while (child != null) {
            if (child.getElementType() == type) {
                return child;
            }
            child = child.getTreePrev();
        }
        return null;
    }

}
