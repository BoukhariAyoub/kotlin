/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class KtSelfType extends KtElementImpl implements KtTypeElement {
    public KtSelfType(@NotNull ASTNode node) {
        super(node);
    }

    @NotNull
    @Override
    public List<KtTypeReference> getTypeArgumentsAsTypes() {
        return Collections.emptyList();
    }

    @Override
    public <R, D> R accept(@NotNull KtVisitor<R, D> visitor, D data) {
        return visitor.visitSelfType(this, data);
    }
}
