/*
 * Copyright (C) 2022 The Android Open Source Project
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

package com.android.server.wm.traces.common.service

import com.android.server.wm.traces.common.transition.Transition

enum class AssertionExecutionCondition(
    val shouldExecute: (transition: Transition) -> Boolean
) {
    ALWAYS({ true }),
    NEVER({ false }),
    APP_LAUNCH({ t ->
        t.type == Transition.Companion.Type.OPEN &&
                t.changes.any { it.transitMode == Transition.Companion.Type.OPEN }
    }),
    APP_CLOSE({ t ->
        t.type == Transition.Companion.Type.CLOSE &&
                t.changes.any { it.transitMode == Transition.Companion.Type.CLOSE }
    })
}