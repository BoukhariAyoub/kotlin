/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.lexer.KtTokens;
import org.jetbrains.kotlin.psi.stubs.KotlinPlaceHolderStub;
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes;

import java.util.List;

public class KtParameterList extends KtElementImplStub<KotlinPlaceHolderStub<KtParameterList>> {
    public KtParameterList(@NotNull ASTNode node) {
        super(node);
    }

    public KtParameterList(@NotNull KotlinPlaceHolderStub<KtParameterList> stub) {
        super(stub, KtStubElementTypes.VALUE_PARAMETER_LIST);
    }

    @Override
    public <R, D> R accept(@NotNull KtVisitor<R, D> visitor, D data) {
        return visitor.visitParameterList(this, data);
    }

    @Override
    public PsiElement getParent() {
        KotlinPlaceHolderStub<KtParameterList> stub = getStub();
        return stub != null ? stub.getParentStub().getPsi() : super.getParent();
    }

    @NotNull
    public List<KtParameter> getParameters() {
        return getStubOrPsiChildrenAsList(KtStubElementTypes.VALUE_PARAMETER);
    }

    @NotNull
    public KtParameter addParameter(@NotNull KtParameter parameter) {
        return EditCommaSeparatedListHelper.INSTANCE.addItem(this, getParameters(), parameter);
    }

    @NotNull
    public KtParameter addParameterBefore(@NotNull KtParameter parameter, @Nullable KtParameter anchor) {
        return EditCommaSeparatedListHelper.INSTANCE.addItemBefore(this, getParameters(), parameter, anchor);
    }

    @NotNull
    public KtParameter addParameterAfter(@NotNull KtParameter parameter, @Nullable KtParameter anchor) {
        return EditCommaSeparatedListHelper.INSTANCE.addItemAfter(this, getParameters(), parameter, anchor);
    }

    public void removeParameter(@NotNull KtParameter parameter) {
        EditCommaSeparatedListHelper.INSTANCE.removeItem(parameter);
    }

    public void removeParameter(int index) {
        removeParameter(getParameters().get(index));
    }

    public KtDeclarationWithBody getOwnerFunction() {
        PsiElement parent = getParentByStub();
        if (!(parent instanceof KtDeclarationWithBody)) return null;
        return (KtDeclarationWithBody) parent;
    }

    @Nullable
    public PsiElement getRightParenthesis() {
        return findChildByType(KtTokens.RPAR);
    }

    @Nullable
    public PsiElement getLeftParenthesis() {
        return findChildByType(KtTokens.LPAR);
    }

    @Nullable
    public PsiElement getFirstComma() {
        return findChildByType(KtTokens.COMMA);
    }
}
