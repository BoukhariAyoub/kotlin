/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.codegen

import org.jetbrains.kotlin.test.ConfigurationKind

class ReflectionClassLoaderTest : CodegenTestCase() {
    override fun getPrefix() = "reflection/classLoaders"

    override fun setUp() {
        super.setUp()
        configurationKind = ConfigurationKind.ALL
        createEnvironmentWithMockJdkAndIdeaAnnotations(configurationKind)
    }

    private fun Class<*>.methodByName(name: String) = declaredMethods.single { it.name == name }

    fun doTest(cl1: ClassLoader, cl2: ClassLoader) {
        val t1 = cl1.loadClass("test.Test")
        val t2 = cl2.loadClass("test.Test")

        fun Class<*>.getKClass() = methodByName("kClass")(newInstance())

        t1.methodByName("doTest")(t1.newInstance(), t1.getKClass(), t2.getKClass())
    }

    fun testSimpleDifferentClassLoaders() {
        loadFile(prefix + "/differentClassLoaders.kt")

        doTest(
                createClassLoader(),
                createClassLoader()
        )
    }

    fun testClassLoaderWithNonTrivialEqualsAndHashCode() {
        // Check that class loaders do not participate as keys in hash maps (use identity hash maps instead)

        loadFile(prefix + "/differentClassLoaders.kt")

        class BrokenEqualsClassLoader(parent: ClassLoader) : ClassLoader(parent) {
            override fun equals(other: Any?) = true
            override fun hashCode() = 0
        }

        doTest(
                BrokenEqualsClassLoader(createClassLoader()),
                BrokenEqualsClassLoader(createClassLoader())
        )
    }

    fun testParentFirst() {
        // Check that for a child class loader, a class reference would be the same as for his parent

        loadFile(prefix + "/parentFirst.kt")

        class ChildClassLoader(parent: ClassLoader) : ClassLoader(parent)

        val parent = createClassLoader()

        doTest(
                parent,
                ChildClassLoader(parent)
        )
    }
}
