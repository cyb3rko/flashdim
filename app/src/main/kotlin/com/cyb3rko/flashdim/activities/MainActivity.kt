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

package com.cyb3rko.flashdim.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibratorManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cyb3rko.flashdim.AppReviewManager
import com.cyb3rko.flashdim.BuildConfig
import com.cyb3rko.flashdim.Camera
import com.cyb3rko.flashdim.MorseHandler
import com.cyb3rko.flashdim.R
import com.cyb3rko.flashdim.databinding.ActivityMainBinding
import com.cyb3rko.flashdim.handleFlashlightException
import com.cyb3rko.flashdim.modals.AboutDialog
import com.cyb3rko.flashdim.modals.DeviceSupportDialog
import com.cyb3rko.flashdim.modals.IntervalDialog
import com.cyb3rko.flashdim.modals.MorseDialog
import com.cyb3rko.flashdim.seekbar.SeekBarChangeListener
import com.cyb3rko.flashdim.utils.DeviceSupportManager
import com.cyb3rko.flashdim.utils.Safe
import com.cyb3rko.flashdim.utils.Vibrator
import com.cyb3rko.flashdim.utils.couldBeRunningOnEmulator
import com.cyb3rko.flashdim.utils.disable
import com.cyb3rko.flashdim.utils.enable
import com.cyb3rko.flashdim.utils.hide
import com.cyb3rko.flashdim.utils.makeInvisible
import com.cyb3rko.flashdim.utils.openUrl
import com.cyb3rko.flashdim.utils.show
import com.cyb3rko.flashdim.utils.showDialog
import kotlin.system.exitProcess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentLevel = -1
    private var maxLevel = -1
    private val camera by lazy { Camera(this) }

    private var settingsOpened = false
    private var morseActivated = false
    private var vibrateButtons = false
    private var vibrateMorse = false
    private var intentFlash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Camera.doesDeviceHaveFlash(packageManager)) {
            setContentView(View(this))
            showDialog(
                title = getString(R.string.dialog_not_supported_title),
                message = getString(R.string.dialog_not_supported_message),
                icon = android.R.drawable.ic_dialog_alert,
                action = { exitProcess(0) },
                actionMessage = getString(R.string.dialog_not_supported_button),
                cancelable = false
            )
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.topAppBar)

        if (camera.idEmpty) return
        maxLevel = camera.maxLevel
        Safe.initialize(this)
        Safe.writeInt(Safe.MAX_LEVEL, maxLevel)
        if (Safe.getInt(Safe.INITIAL_LEVEL, -1) == -1) {
            Safe.writeInt(Safe.INITIAL_LEVEL, maxLevel)
        }

        if (maxLevel > 1 || couldBeRunningOnEmulator()) {
            initSeekbar()
            Safe.writeBoolean(Safe.MULTILEVEL, true)
            handleShortCutIntent()
        } else {
            switchToSimpleMode()
            Safe.writeBoolean(Safe.MULTILEVEL, false)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (!Camera.doesDeviceHaveFlash(packageManager)) return
        initButtonClickListeners()
        checkDeviceSupport()
        if (!Safe.getBoolean(Safe.FLASH_ACTIVE, false) && !intentFlash) {
            executeAppStartFlash()
        }

        vibrateButtons = Safe.getBoolean(Safe.BUTTON_VIBRATION, true)
        vibrateMorse = Safe.getBoolean(Safe.MORSE_VIBRATION, true)
        val systemVibrator = (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
            .defaultVibrator
        Vibrator.initialize(systemVibrator)
        AppReviewManager.initiateReviewDialog(this)
    }

    override fun onResume() {
        super.onResume()
        val restored = restoreLightLevelUi()
        if (!restored) executeAppOpenFlash()
        intentFlash = false
    }

    private fun handleShortCutIntent() {
        if (intent.action == Intent.ACTION_MAIN || intent.action == Intent.ACTION_VIEW) return
        intentFlash = true
        val newLevel = when (intent.action) {
            "DIMMER_MIN" -> 1
            "DIMMER_HALF" -> maxLevel / 2
            "DIMMER_MAX" -> maxLevel
            else -> -1
        }
        camera.sendLightLevel(this, currentLevel, newLevel)
        updateLightLevelView(if (newLevel > 0) newLevel else 0)
        binding.seekBar.setProgress(if (newLevel > 0) newLevel else 0)
        currentLevel = newLevel
    }

    override fun onPause() {
        super.onPause()
        if (!settingsOpened && Safe.getBoolean(Safe.PAUSE_FLASH, false)) {
            updateLightLevelView(0)
            binding.seekBar.setProgress(0)
            camera.setTorchMode(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        IntervalDialog.stop()
    }

    private fun initSeekbar() {
        binding.seekBar.maxProgress = maxLevel
        binding.seekBar.onProgressChanged = object : SeekBarChangeListener {
            override fun onProgressChanged(progress: Int) {
                if (progress > 0) {
                    if (progress <= maxLevel) {
                        if (vibrateButtons) Vibrator.vibrateTick()
                        camera.sendLightLevel(this@MainActivity, currentLevel, progress)
                        updateLightLevelView(progress)
                    } else {
                        camera.sendLightLevel(this@MainActivity, currentLevel, maxLevel)
                        updateLightLevelView(maxLevel)
                    }
                    currentLevel = progress
                } else if (progress == 0) {
                    camera.setTorchMode(false)
                    updateLightLevelView(0)
                    currentLevel = 0
                }
            }
        }
    }

    private fun initButtonClickListeners() {
        binding.apply {
            sosButton.setOnClickListener {
                sosButton.disable()
                morseButton.hide()
                intervalButton.hide()
                switchMorseMode(true, getString(R.string.light_level_sos))
                handleMorseCall("SOS")
            }
            morseButton.setOnClickListener {
                showMorseDialog()
            }
            intervalButton.setOnClickListener {
                IntervalDialog.show(this@MainActivity) { enabled ->
                    if (enabled) {
                        camera.setTorchMode(true)
                    } else {
                        camera.setTorchMode(false)
                    }
                }
            }
            maxButton.setOnClickListener {
                if (vibrateButtons) Vibrator.vibrateDoubleClick()
                if (isDimAllowed()) {
                    updateLightLevelView(maxLevel)
                    camera.sendLightLevel(this@MainActivity, currentLevel, maxLevel)
                    currentLevel = maxLevel
                    seekBar.setProgress(maxLevel)
                } else {
                    camera.setTorchMode(true)
                }
            }
            halfButton.setOnClickListener {
                if (vibrateButtons) Vibrator.vibrateClick()
                updateLightLevelView(maxLevel / 2)
                camera.sendLightLevel(this@MainActivity, currentLevel, maxLevel / 2)
                currentLevel = maxLevel / 2
                seekBar.setProgress(maxLevel / 2)
            }
            minButton.setOnClickListener {
                if (vibrateButtons) Vibrator.vibrateClick()
                updateLightLevelView(1)
                camera.sendLightLevel(this@MainActivity, currentLevel, 1)
                currentLevel = 1
                seekBar.setProgress(1)
            }
            offButton.setOnClickListener {
                if (vibrateButtons) Vibrator.vibrateClick()
                morseActivated = false
                if (isDimAllowed()) {
                    updateLightLevelView(0)
                    currentLevel = 0
                    seekBar.setProgress(0)
                }
                camera.setTorchMode(false)
            }
            if (BuildConfig.DEBUG) {
                offButton.setOnLongClickListener {
                    switchToSimpleMode()
                    checkDeviceSupport(true)
                    true
                }
            }
        }
    }

    private fun checkDeviceSupport(forceShow: Boolean = false) {
        if (forceShow) {
            showDeviceChipAndDialog(true)
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val excluded = DeviceSupportManager.isExcluded(resources)
            withContext(Dispatchers.Main) {
                if (excluded && maxLevel > 1) {
                    showDeviceChipAndDialog(false)
                } else if (!excluded && maxLevel <= 1) {
                    showDeviceChipAndDialog(true)
                }
            }
        }
    }

    private fun showDeviceChipAndDialog(newReport: Boolean) {
        @SuppressLint("SetTextI18n")
        binding.deviceName.text = "${Build.MANUFACTURER} ${Build.MODEL} (${Build.DEVICE})"
        binding.deviceName.setChipIconResource(android.R.drawable.ic_dialog_alert)
        if (newReport) {
            binding.deviceName.textSize = 12f
            binding.deviceName.setOnClickListener {
                DeviceSupportDialog.show(this@MainActivity, maxLevel, true)
            }
            DeviceSupportDialog.show(this@MainActivity, maxLevel, false)
        } else {
            binding.deviceName.setOnClickListener {
                DeviceSupportDialog.showWrong(this@MainActivity, maxLevel, true)
            }
            DeviceSupportDialog.showWrong(this@MainActivity, maxLevel, false)
        }
        binding.deviceName.show()
    }

    private fun executeAppStartFlash() {
        if (Safe.getBoolean(Safe.APPSTART_FLASH, false)) {
            activateInitialFlash()
        } else if (!intentFlash) {
            updateLightLevelView(0)
        }
    }

    private fun executeAppOpenFlash() {
        if (!settingsOpened && Safe.getBoolean(Safe.APPSTART_FLASH, false) &&
            Safe.getBoolean(Safe.APPOPEN_FLASH, false)
        ) {
            activateInitialFlash()
        } else {
            settingsOpened = false
        }
    }

    private fun activateInitialFlash() {
        if (maxLevel > 1) {
            val level = Safe.getInt(Safe.INITIAL_LEVEL, -1)
            camera.sendLightLevel(this@MainActivity, currentLevel, level)
            updateLightLevelView(level)
            binding.seekBar.setProgress(level)
        } else {
            camera.setTorchMode(true)
        }
    }

    private fun restoreLightLevelUi(): Boolean {
        val restored = if (maxLevel > 1 && Safe.getBoolean(Safe.FLASH_ACTIVE, false)) {
            val level = if (Safe.getBoolean(Safe.QUICK_SETTINGS_LINK, false)) {
                Safe.getInt(Safe.INITIAL_LEVEL, 0)
            } else {
                maxLevel
            }
            updateLightLevelView(level)
            binding.seekBar.setProgress(level)
            true
        } else {
            false
        }
        Safe.writeBoolean(Safe.FLASH_ACTIVE, false)
        return restored
    }

    private fun switchToSimpleMode() {
        binding.apply {
            buttonContainer.setPadding(0, 24, 64, 24)
            maxButton.text = getString(R.string.button_max_on)
            halfButton.hide()
            minButton.hide()
            seekBar.hide()
            levelIndicatorDesc.makeInvisible()
            levelIndicator.makeInvisible()
            errorView.text = getString(R.string.hint_dim_not_supported)
            errorView.show()
            quickActionsView.hide()
        }
    }

    private fun showMorseDialog() {
        MorseDialog.show(this) { message ->
            binding.sosButton.hide()
            binding.morseButton.disable()
            binding.intervalButton.hide()
            switchMorseMode(true, getString(R.string.light_level_morse))
            handleMorseCall(message)
        }
    }

    private fun switchMorseMode(activate: Boolean, message: String = "") {
        if (activate) {
            camera.setTorchMode(false)
            morseActivated = true
            binding.apply {
                binding.seekBar.setProgress(0)
                maxButton.hide()
                halfButton.hide()
                minButton.hide()
                seekBar.disable()
                binding.levelIndicator.text = message
            }
        } else {
            binding.apply {
                quickActionsView.text = getString(R.string.textview_quick_actions_title)
                maxButton.show()
                sosButton.enable()
                sosButton.show()
                morseButton.enable()
                morseButton.show()
                intervalButton.enable()
                intervalButton.show()
                if (isDimAllowed()) {
                    halfButton.show()
                    minButton.show()
                    seekBar.enable()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateLightLevelView(level: Int, note: String = "") {
        binding.levelIndicator.text = "$level / $maxLevel$note"
    }

    private fun isDimAllowed() = binding.maxButton.text == getString(R.string.button_max_maximum)

    private fun handleMorseCall(message: String) {
        val morseExceptionHandler = CoroutineExceptionHandler { _, error ->
            switchMorseMode(false)
            binding.levelIndicator.text = "0"
            updateLightLevelView(0)
            handleFlashlightException(error as Exception, this@MainActivity)
        }
        lifecycleScope.launch(morseExceptionHandler) {
            var lastLetter = Char.MIN_VALUE
            val handler = MorseHandler { letter, code, delay, on ->
                camera.setTorchMode(on)
                if (vibrateMorse && on) Vibrator.vibrate(delay)

                if (lastLetter != letter) {
                    binding.quickActionsView.text = getString(
                        R.string.textview_quick_actions_morse,
                        letter,
                        code
                    )

                    lastLetter = letter
                }

                morseActivated
            }
            while (morseActivated) {
                handler.flashMessage(message)
                if (morseActivated) handler.waitForRepeat()
            }
            switchMorseMode(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.icon_credits_action -> {
                AboutDialog.show(this, maxLevel)
                return true
            }
            R.id.settings_action -> {
                settingsOpened = true
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            R.id.github_action -> {
                openUrl("https://github.com/cyb3rko/flashdim", "GitHub Repo")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
