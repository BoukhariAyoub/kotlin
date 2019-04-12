/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.cli.jvm.javac

import org.jetbrains.kotlin.asJava.LightClassGenerationSupport
import org.jetbrains.kotlin.asJava.findFacadeClass
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.javac.JavacWrapperKotlinResolver
import org.jetbrains.kotlin.javac.resolve.MockKotlinField
import org.jetbrains.kotlin.load.java.structure.JavaField
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.classId

class JavacWrapperKotlinResolverImpl(private val lightClassGenerationSupport: LightClassGenerationSupport) : JavacWrapperKotlinResolver {

    private val supersCache = hashMapOf<KtClassOrObject, List<ClassId>>()

    override fun resolveSupertypes(classOrObject: KtClassOrObject): List<ClassId> {
        if (supersCache.containsKey(classOrObject)) {
            return supersCache[classOrObject]!!
        }

        val classDescriptor =
            lightClassGenerationSupport.analyze(classOrObject).get(BindingContext.CLASS, classOrObject) ?: return emptyList()
        val classIds = classDescriptor.defaultType.constructor.supertypes
            .mapNotNull { (it.constructor.declarationDescriptor as? ClassDescriptor)?.classId }
        supersCache[classOrObject] = classIds

        return classIds
    }

    override fun findField(classOrObject: KtClassOrObject, name: String): JavaField? {
        val lightClass = classOrObject.toLightClass() ?: return null

        return lightClass.allFields.find { it.name == name }?.let(::MockKotlinField)
    }

    override fun findField(ktFile: KtFile?, name: String): JavaField? {
        val lightClass = ktFile?.findFacadeClass() ?: return null

        return lightClass.allFields.find { it.name == name }?.let(::MockKotlinField)
    }
}
