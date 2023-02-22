/*
 * Copyright (C) 2023 The Android Open Source Project
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

package android.tools.common.flicker.assertors.assertions

import android.tools.common.datatypes.component.ComponentNameMatcher
import android.tools.common.flicker.IScenarioInstance
import android.tools.common.flicker.assertors.ComponentTemplate
import android.tools.common.flicker.subject.layers.LayersTraceSubject

/**
 * Asserts that:
 * ```
 *     [Components.LAUNCHER] is visible at the start of the trace
 *     [Components.LAUNCHER] becomes invisible during the trace and (in the same entry)
 *     [component] becomes visible
 *     [component] remains visible until the end of the trace
 * ```
 */
class AppLayerReplacesLauncher(private val component: ComponentTemplate) :
    AssertionTemplateWithComponent(component) {
    /** {@inheritDoc} */
    override fun doEvaluate(scenarioInstance: IScenarioInstance, layerSubject: LayersTraceSubject) {
        layerSubject
            .isVisible(ComponentNameMatcher.LAUNCHER)
            .then()
            .isVisible(component.build(scenarioInstance))
            .forAllEntries()
    }
}
