/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.wm.flicker.assertions

import com.android.server.wm.flicker.AssertionTag
import com.android.server.wm.flicker.io.IReader
import com.android.server.wm.flicker.traces.FlickerSubjectException
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path

class FlickerAssertionErrorBuilder {
    private var error: Throwable? = null
    private var artifactPath: Path? = null
    private var tag = ""

    fun fromError(error: Throwable): FlickerAssertionErrorBuilder = apply { this.error = error }

    fun withReader(reader: IReader): FlickerAssertionErrorBuilder = apply {
        artifactPath = reader.artifactPath
    }

    fun atTag(_tag: String): FlickerAssertionErrorBuilder = apply {
        tag =
            when (_tag) {
                AssertionTag.START -> "before transition (initial state)"
                AssertionTag.END -> "after transition (final state)"
                AssertionTag.ALL -> "during transition"
                else -> "at user-defined location ($_tag)"
            }
    }

    fun build(): FlickerAssertionError {
        return FlickerAssertionError(errorMessage, rootCause)
    }

    private val errorMessage
        get() = buildString {
            val error = error
            requireNotNull(error)
            if (error is FlickerSubjectException) {
                appendLine(error.message)
                appendLine()
                append("\t").appendLine(Fact("Location", tag))
                appendLine()
            } else {
                appendLine(error.message)
            }
            append("Trace file:").append(traceFileMessage)
            appendLine()
            appendLine("Cause:")
            append(rootCauseStackTrace)
            appendLine()
            appendLine("Full stacktrace:")
            appendLine()
        }

    private val traceFileMessage
        get() = buildString {
            artifactPath?.let {
                append("\t")
                append(it)
            }
        }

    private val rootCauseStackTrace: String
        get() {
            val rootCause = rootCause
            return if (rootCause != null) {
                val baos = ByteArrayOutputStream()
                PrintStream(baos, true).use { ps -> rootCause.printStackTrace(ps) }
                "\t$baos"
            } else {
                ""
            }
        }

    /**
     * In some paths the exceptions are encapsulated by the Truth subjects To make sure the correct
     * error is printed, located the first non-subject related exception and use that as cause.
     */
    private val rootCause: Throwable?
        get() {
            var childCause: Throwable? = this.error?.cause
            if (childCause != null && childCause is FlickerSubjectException) {
                childCause = childCause.cause
            }
            return childCause
        }
}
