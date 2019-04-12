/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Project contributors. Use of this source code is governed
 * by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.extensions

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.utils.addToStdlib.firstNotNullResult

/**
 * The interface for the extensions that are used to substitute VirtualFile on the creation of KtFile, allows to preprocess a file before
 * lexing and parsing
 */
interface PreprocessedVirtualFileFactoryExtension {
    companion object : ProjectExtensionDescriptor<PreprocessedVirtualFileFactoryExtension>(
        "org.jetbrains.kotlin.preprocessedVirtualFileFactoryExtension",
        PreprocessedVirtualFileFactoryExtension::class.java
    )

    fun isPassThrough(): Boolean

    fun createPreprocessedFile(file: VirtualFile?): VirtualFile?
    fun createPreprocessedLightFile(file: LightVirtualFile?): LightVirtualFile?
}

class PreprocessedFileCreator(val project: Project) {

    private val validExts: Array<PreprocessedVirtualFileFactoryExtension> by lazy {
        PreprocessedVirtualFileFactoryExtension.getInstances(project).filterNot { it.isPassThrough() }.toTypedArray()
    }

    fun create(file: VirtualFile): VirtualFile = validExts.firstNotNullResult { it.createPreprocessedFile(file) } ?: file

    // unused now, but could be used in the IDE at some point
    fun createLight(file: LightVirtualFile): LightVirtualFile =
        validExts.firstNotNullResult { it.createPreprocessedLightFile(file) } ?: file
}

