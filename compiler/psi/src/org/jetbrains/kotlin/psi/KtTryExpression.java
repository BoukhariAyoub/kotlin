/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.KtNodeTypes;
import org.jetbrains.kotlin.lexer.KtTokens;

import java.util.List;

public class KtTryExpression extends KtExpressionImpl {
    public KtTryExpression(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public <R, D> R accept(@NotNull KtVisitor<R, D> visitor, D data) {
        return visitor.visitTryExpression(this, data);
    }

    @NotNull
    public KtBlockExpression getTryBlock() {
        return (KtBlockExpression) findChildByType(KtNodeTypes.BLOCK);
    }

    @NotNull
    public List<KtCatchClause> getCatchClauses() {
        return findChildrenByType(KtNodeTypes.CATCH);
    }

    @Nullable
    public KtFinallySection getFinallyBlock() {
        return (KtFinallySection) findChildByType(KtNodeTypes.FINALLY);
    }

    @Nullable
    public PsiElement getTryKeyword() {
        return findChildByType(KtTokens.TRY_KEYWORD);
    }
}
