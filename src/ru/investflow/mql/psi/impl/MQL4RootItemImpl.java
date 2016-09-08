package ru.investflow.mql.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import ru.investflow.mql.psi.MQL4Comment;
import ru.investflow.mql.psi.MQL4RootItem;
import ru.investflow.mql.psi.MQL4TopLevelDeclaration;
import ru.investflow.mql.psi.MQL4Visitor;

public class MQL4RootItemImpl extends ASTWrapperPsiElement implements MQL4RootItem {

  public MQL4RootItemImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MQL4Visitor) ((MQL4Visitor)visitor).visitRootItem(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public MQL4Comment getComment() {
    return findChildByClass(MQL4Comment.class);
  }

  @Override
  @Nullable
  public MQL4TopLevelDeclaration getTopLevelDeclaration() {
    return findChildByClass(MQL4TopLevelDeclaration.class);
  }

}
