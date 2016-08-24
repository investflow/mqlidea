// This is a generated file. Not intended for manual editing.
package ru.investflow.mql.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import ru.investflow.mql.psi.MQL4Literal;
import ru.investflow.mql.psi.MQL4PreprocessorPropertyBlock;
import ru.investflow.mql.psi.MQL4Visitor;

import static ru.investflow.mql.psi.MQL4TokenTypes.IDENTIFIER;

public class MQL4PreprocessorPropertyBlockImpl extends ASTWrapperPsiElement implements MQL4PreprocessorPropertyBlock {

  public MQL4PreprocessorPropertyBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MQL4Visitor) ((MQL4Visitor)visitor).visitPreprocessorPropertyBlock(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public MQL4Literal getLiteral() {
    return findChildByClass(MQL4Literal.class);
  }

  @Override
  @NotNull
  public PsiElement getId() {
    return findNotNullChildByType(IDENTIFIER);
  }

}
