// This is a generated file. Not intended for manual editing.
package ru.investflow.mql.psi.impl;

import org.jetbrains.annotations.NotNull;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import ru.investflow.mql.psi.MQL4PreprocessorBlock;
import ru.investflow.mql.psi.MQL4PreprocessorPropertyBlock;
import ru.investflow.mql.psi.MQL4Visitor;

public class MQL4PreprocessorBlockImpl extends ASTWrapperPsiElement implements MQL4PreprocessorBlock {

  public MQL4PreprocessorBlockImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MQL4Visitor) ((MQL4Visitor)visitor).visitPreprocessorBlock(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public MQL4PreprocessorPropertyBlock getPreprocessorPropertyBlock() {
    return findNotNullChildByClass(MQL4PreprocessorPropertyBlock.class);
  }

}
