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

package com.android.server.wm.traces.common.events

import com.android.server.wm.traces.common.ITrace
import kotlin.js.JsName

/**
 * Represents the data from the Android EventLog and contains a collection of parsed events of
 * interest.
 *
 * This is a generic object that is reused by both Flicker and Winscope and cannot access internal
 * Java/Android functionality
 */
class EventLog(override val entries: Array<Event>) :
    ITrace<Event>, List<Event> by entries.toList() {
    @JsName("focusEvents")
    val focusEvents: Array<FocusEvent>
        get() =
            entries
                .filterIsInstance<FocusEvent>()
                .filter { it.type !== FocusEvent.Type.REQUESTED }
                .toTypedArray()

    @JsName("cujEvents")
    val cujEvents: Array<CujEvent>
        get() = entries.filterIsInstance<CujEvent>().toTypedArray()

    companion object {
        const val MAGIC_NUMBER = "EventLog"
    }
}
