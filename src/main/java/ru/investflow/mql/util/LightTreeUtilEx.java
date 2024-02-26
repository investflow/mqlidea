package ru.investflow.mql.util;

import com.intellij.lang.LighterAST;
import com.intellij.lang.LighterASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightTreeUtilEx {

    @Nullable
    public static LighterASTNode prevSiblingOfType(@NotNull LighterAST tree, @NotNull LighterASTNode parentNode, @NotNull LighterASTNode node, @NotNull IElementType type) {
        List<LighterASTNode> children = tree.getChildren(parentNode);
        int nodeIdx = getChildIndex(node, children);
        if (nodeIdx < 0) {
            return null;
        }
        for (int i = nodeIdx; --i >= 0; ) {
            LighterASTNode prev = children.get(i);
            if (prev.getTokenType() == type) {
                return prev;
            }
        }
        return null;
    }

    private static int getChildIndex(@NotNull LighterASTNode node, @NotNull List<LighterASTNode> children) {
        for (int i = 0; i < children.size(); i++) {
            LighterASTNode n = children.get(i);
            if (n.getTokenType() == node.getTokenType() && n.getStartOffset() == node.getStartOffset()) {
                return i;
            }
            if (n.getStartOffset() > n.getStartOffset()) {
                break;
            }
        }
        return -1;
    }
}
