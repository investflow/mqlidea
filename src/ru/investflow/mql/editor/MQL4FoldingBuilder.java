package ru.investflow.mql.editor;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.investflow.mql.psi.MQL4Elements;
import ru.investflow.mql.psi.MQL4TokenSets;

import java.util.ArrayList;
import java.util.List;

/**
 * MQL4 code folding. Supports folding of comments only today.
 */
public class MQL4FoldingBuilder implements FoldingBuilder, DumbAware {
    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        final List<FoldingDescriptor> descriptors = new ArrayList<>();
        collectDescriptorsRecursively(node, document, descriptors);
        return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
    }

    private static void collectDescriptorsRecursively(@NotNull ASTNode node, @NotNull Document document, @NotNull List<FoldingDescriptor> descriptors) {
        IElementType type = node.getElementType();
        if (type == MQL4Elements.BLOCK_COMMENT) {
            descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
        } else if (type == MQL4Elements.LINE_COMMENT) {
            Couple<PsiElement> commentRange = expandLineCommentsRange(node.getPsi());
            int startOffset = commentRange.getFirst().getTextRange().getStartOffset();
            int endOffset = commentRange.getSecond().getTextRange().getEndOffset() - 1;
            if (document.getLineNumber(startOffset) != document.getLineNumber(endOffset)) {
                descriptors.add(new FoldingDescriptor(node, new TextRange(startOffset, endOffset)));
            }
        }
        for (ASTNode child : node.getChildren(null)) {
            collectDescriptorsRecursively(child, document, descriptors);
        }
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        IElementType type = node.getElementType();
        if (type == MQL4Elements.LINE_COMMENT) {
            return "//...";
        } else if (type == MQL4Elements.BLOCK_COMMENT) {
            return "/*...*/";
        }
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }

    @NotNull
    public static Couple<PsiElement> expandLineCommentsRange(@NotNull PsiElement anchor) {
        return Couple.of(findFurthestSiblingOfSameType(anchor, false), findFurthestSiblingOfSameType(anchor, true));
    }

    /**
     * Find the furthest sibling element with the same type as given anchor.
     * <p/>
     * Ignore white spaces for any type of element except {@link ru.investflow.mql.psi.MQL4Elements#LINE_COMMENT}
     * where non indentation white space (that has new line in the middle) will stop the search.
     *
     * @param anchor element to start from
     * @param after  whether to scan through sibling elements forward or backward
     * @return described element or anchor if search stops immediately
     */
    @NotNull
    public static PsiElement findFurthestSiblingOfSameType(@NotNull PsiElement anchor, boolean after) {
        ASTNode node = anchor.getNode();
        // Compare by node type to distinguish between different types of comments
        IElementType expectedType = node.getElementType();
        ASTNode lastSeen = node;
        while (node != null) {
            IElementType elementType = node.getElementType();
            if (elementType == expectedType) {
                lastSeen = node;
            } else if (elementType == MQL4Elements.WHITE_SPACE) {
                if (expectedType == MQL4Elements.LINE_COMMENT && node.getText().indexOf('\n', 1) != -1) {
                    break;
                }
            } else if (!MQL4TokenSets.COMMENTS.contains(elementType) || MQL4TokenSets.COMMENTS.contains(expectedType)) {
                break;
            }
            node = after ? node.getTreeNext() : node.getTreePrev();
        }
        return lastSeen.getPsi();
    }
}
