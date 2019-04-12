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

public class KtReturnExpression extends KtExpressionWithLabel implements KtStatementExpression {
    public KtReturnExpression(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public <R, D> R accept(@NotNull KtVisitor<R, D> visitor, D data) {
        return visitor.visitReturnExpression(this, data);
    }

    @Nullable
    public KtExpression getReturnedExpression() {
        return findChildByClass(KtExpression.class);
    }

    @NotNull
    public PsiElement getReturnKeyword() {
        //noinspection ConstantConditions
        return findChildByType(KtTokens.RETURN_KEYWORD);
    }

    @Nullable
    public PsiElement getLabeledExpression() {
        return findChildByType(KtNodeTypes.LABEL_QUALIFIER);
    }
}
