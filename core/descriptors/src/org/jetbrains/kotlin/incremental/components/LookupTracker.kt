/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.incremental.components

import org.jetbrains.kotlin.container.DefaultImplementation
import java.io.Serializable

@DefaultImplementation(LookupTracker.DO_NOTHING::class)
interface LookupTracker {
    // used in tests for more accurate checks
    val requiresPosition: Boolean

    fun record(
            filePath: String,
            position: Position,
            scopeFqName: String,
            scopeKind: ScopeKind,
            name: String
    )

    object DO_NOTHING : LookupTracker {
        override val requiresPosition: Boolean
            get() = false

        override fun record(filePath: String, position: Position, scopeFqName: String, scopeKind: ScopeKind, name: String) {
        }
    }
}

enum class ScopeKind {
    PACKAGE,
    CLASSIFIER
}

data class LookupInfo(
        val filePath: String,
        val position: Position,
        val scopeFqName: String,
        val scopeKind: ScopeKind,
        val name: String
) : Serializable
