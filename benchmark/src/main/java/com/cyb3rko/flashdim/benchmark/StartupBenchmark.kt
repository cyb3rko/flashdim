/*
 * Copyright (c) 2023 Cyb3rKo
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

package com.cyb3rko.flashdim.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is the startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * Switch your app's active build variant in the Studio (affects Studio runs only)
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startUpColdNaked() = startup(StartupMode.COLD, CompilationMode.None())

    @Test
    fun startUpColdBaseline() = startup(StartupMode.COLD, CompilationMode.Partial())

    @Test
    fun startUpWarmNaked() = startup(StartupMode.WARM, CompilationMode.None())

    @Test
    fun startUpWarmBaseline() = startup(StartupMode.HOT, CompilationMode.Partial())

    @Test
    fun startUpHotNaked() = startup(StartupMode.HOT, CompilationMode.None())

    @Test
    fun startUpHotBaseline() = startup(StartupMode.HOT, CompilationMode.Partial())

    private fun startup(
        startupMode: StartupMode,
        compilationMode: CompilationMode
    ) = benchmarkRule.measureRepeated(
        packageName = Constants.PACKAGE_NAME,
        metrics = listOf(StartupTimingMetric()),
        iterations = Constants.STARTUP_ITERATIONS,
        startupMode = startupMode,
        compilationMode = compilationMode,
        setupBlock = {
            pressHome()
        }
    ) {
        startActivityAndWait()
    }
}
