/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.KtNodeTypes;

import java.util.List;

public class KtCatchClause extends KtElementImpl {
    public KtCatchClause(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public <R, D> R accept(@NotNull KtVisitor<R, D> visitor, D data) {
        return visitor.visitCatchSection(this, data);
    }

    @Nullable @IfNotParsed
    public KtParameterList getParameterList() {
        return (KtParameterList) findChildByType(KtNodeTypes.VALUE_PARAMETER_LIST);
    }

    @Nullable @IfNotParsed
    public KtParameter getCatchParameter() {
        KtParameterList list = getParameterList();
        if (list == null) return null;
        List<KtParameter> parameters = list.getParameters();
        return parameters.size() == 1 ? parameters.get(0) : null;
    }


    @Nullable @IfNotParsed
    public KtExpression getCatchBody() {
        return findChildByClass(KtExpression.class);
    }
}
