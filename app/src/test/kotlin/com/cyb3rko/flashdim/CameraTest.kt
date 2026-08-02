/*
 * Copyright (c) 2026 Isaac Weber
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyb3rko.flashdim

import org.junit.Assert.assertEquals
import org.junit.Test

class CameraTest {
    @Test
    fun `preferred level above new maximum is clamped`() {
        assertEquals(21, clampFlashStrengthLevel(level = 45, maximumLevel = 21))
    }

    @Test
    fun `preferred level within supported range is unchanged`() {
        assertEquals(10, clampFlashStrengthLevel(level = 10, maximumLevel = 21))
    }

    @Test
    fun `preferred level below supported range is clamped`() {
        assertEquals(1, clampFlashStrengthLevel(level = 0, maximumLevel = 21))
    }
}
