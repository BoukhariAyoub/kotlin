/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.storage

import kotlin.reflect.KProperty

interface MemoizedFunctionToNotNull<in P, out R : Any> : Function1<P, R> {
    fun isComputed(key: P): Boolean
}

interface MemoizedFunctionToNullable<in P, out R : Any> : Function1<P, R?> {
    fun isComputed(key: P): Boolean
}

interface NotNullLazyValue<out T : Any> : Function0<T> {
    fun isComputed(): Boolean
    fun isComputing(): Boolean

    // Only for debugging
    fun renderDebugInformation(): String = ""
}

interface NullableLazyValue<out T : Any> : Function0<T?> {
    fun isComputed(): Boolean
    fun isComputing(): Boolean
}

operator fun <T : Any> NotNullLazyValue<T>.getValue(_this: Any?, p: KProperty<*>): T = invoke()

operator fun <T : Any> NullableLazyValue<T>.getValue(_this: Any?, p: KProperty<*>): T? = invoke()

interface CacheWithNullableValues<in K, V : Any> {
    fun computeIfAbsent(key: K, computation: () -> V?): V?
}

interface CacheWithNotNullValues<in K, V : Any> {
    fun computeIfAbsent(key: K, computation: () -> V): V
}
