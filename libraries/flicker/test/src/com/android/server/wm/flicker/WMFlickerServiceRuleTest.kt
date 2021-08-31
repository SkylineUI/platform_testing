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

package com.android.server.wm.flicker

import com.android.server.wm.flicker.rules.WMFlickerServiceRule
import com.google.common.truth.Truth
import org.junit.runner.Description

/**
 * An extension for [WMFlickerServiceRule] checking that the traces are collected.
 */
class WMFlickerServiceRuleTest : WMFlickerServiceRule() {
    override fun finished(description: Description?) {
        super.finished(description)

        Truth.assertThat(wmTrace).isNotEmpty()
        Truth.assertThat(layersTrace).isNotEmpty()
    }
}