// This is a generated file. Not intended for manual editing.
package ru.investflow.mql.psi.impl;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import ru.investflow.mql.psi.MQL4Comment;
import ru.investflow.mql.psi.MQL4Visitor;

public class MQL4CommentImpl extends ASTWrapperPsiElement implements MQL4Comment {

  public MQL4CommentImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MQL4Visitor) ((MQL4Visitor)visitor).visitComment(this);
    else super.accept(visitor);
  }

}
