package ru.investflow.mql.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import ru.investflow.mql.psi.MQL4Literal;
import ru.investflow.mql.psi.MQL4Visitor;

import static ru.investflow.mql.psi.MQL4TokenTypes.INTEGER_LITERAL;
import static ru.investflow.mql.psi.MQL4TokenTypes.STRING_LITERAL;


public class MQL4LiteralImpl extends ASTWrapperPsiElement implements MQL4Literal {

    public MQL4LiteralImpl(ASTNode node) {
        super(node);
    }

    public void accept(@NotNull PsiElementVisitor visitor) {
        if (visitor instanceof MQL4Visitor) {
            ((MQL4Visitor) visitor).visitLiteral(this);
        } else {
            super.accept(visitor);
        }
    }

    @Override
    @Nullable
    public PsiElement getNumber() {
        return findChildByType(INTEGER_LITERAL);
    }

    @Override
    @Nullable
    public PsiElement getString() {
        return findChildByType(STRING_LITERAL);
    }

}
