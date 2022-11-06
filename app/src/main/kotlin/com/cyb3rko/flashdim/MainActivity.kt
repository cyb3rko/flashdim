package com.cyb3rko.flashdim

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.VibratorManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cyb3rko.flashdim.databinding.ActivityMainBinding
import com.cyb3rko.flashdim.seekbar.SeekBarChangeListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.system.exitProcess
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val cameraId by lazy { cameraManager.cameraIdList[0] }
    private val cameraManager by lazy { getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    private var currentLevel = -1
    private var maxLevel = -1
    private val vibrator by lazy {
        (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    }

    private var morseActivated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!doesDeviceHaveFlash()) {
            setContentView(View(this))
            showDialog(
                getString(R.string.dialog_not_supported_title),
                getString(R.string.dialog_not_supported_message),
                { exitProcess(0) },
                getString(R.string.dialog_not_supported_button),
                false
            )
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.topAppBar)

        val cameraInfo = cameraManager.getCameraCharacteristics(cameraId)
        maxLevel = cameraInfo[CameraCharacteristics.FLASH_INFO_STRENGTH_MAXIMUM_LEVEL] ?: -1

        if (maxLevel > 1) {
            binding.seekBar.apply {
                maxProgress = maxLevel
                onProgressChanged = object : SeekBarChangeListener {
                    override fun onProgressChanged(progress: Int) {
                        if (progress > 0) {
                            if (progress <= maxLevel) {
                                Vibrator.vibrateTick(vibrator)
                                cameraManager.sendLightLevel(progress)
                                updateLightLevelView(progress)
                                currentLevel = progress
                            } else {
                                cameraManager.sendLightLevel(maxLevel)
                                updateLightLevelView(maxLevel)
                                currentLevel = progress
                            }
                        } else if (progress == 0) {
                            cameraManager.setTorchMode(cameraId, false)
                            updateLightLevelView(0)
                            currentLevel = 0
                        }
                    }
                }
            }
            if (intent.extras?.getBoolean(SETTINGS_TILE_CLICKED) == null) {
                cameraManager.setTorchMode(cameraId, false)
                updateLightLevelView(0)
            }
        } else {
            switchToSimpleMode()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (!doesDeviceHaveFlash()) return
        binding.apply {
            sosButton.setOnClickListener {
                sosButton.disable()
                morseButton.hide()
                switchMorseMode(true, getString(R.string.light_level_sos))
                handleMorseCall("SOS")
            }
            morseButton.setOnClickListener {
                showMorseDialog()
            }
            maxButton.setOnClickListener {
                Vibrator.vibrateDoubleClick(vibrator)
                if (isDimAllowed()) {
                    updateLightLevelView(maxLevel)
                    cameraManager.sendLightLevel(maxLevel)
                    currentLevel = maxLevel
                    seekBar.setProgress(maxLevel)
                } else {
                    cameraManager.setTorchMode(cameraId, true)
                }
            }
            halfButton.setOnClickListener {
                Vibrator.vibrateClick(vibrator)
                updateLightLevelView(maxLevel / 2)
                cameraManager.sendLightLevel(maxLevel / 2)
                currentLevel = maxLevel / 2
                seekBar.setProgress(maxLevel / 2)
            }
            minButton.setOnClickListener {
                Vibrator.vibrateClick(vibrator)
                updateLightLevelView(1)
                cameraManager.sendLightLevel(1)
                currentLevel = 1
                seekBar.setProgress(1)
            }
            offButton.setOnClickListener {
                Vibrator.vibrateClick(vibrator)
                morseActivated = false
                if (isDimAllowed()) {
                    updateLightLevelView(0)
                    currentLevel = 0
                    seekBar.setProgress(0)
                }
                cameraManager.setTorchMode(cameraId, false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (intent.extras?.getBoolean(SETTINGS_TILE_CLICKED) == true) {
            cameraManager.setTorchMode(cameraId, true)
            updateLightLevelView(maxLevel)
            binding.seekBar.setProgress(maxLevel)
            intent.removeExtra(SETTINGS_TILE_CLICKED)
        }
    }

    @SuppressLint("SetTextI18n")
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
        @SuppressLint("InflateParams")
        val inputLayout = layoutInflater.inflate(R.layout.dialog_morse_input, null)
            .findViewById<TextInputLayout>(R.id.text_input_layout)

        @SuppressLint("InflateParams")
        val inputText = inputLayout.findViewById<TextInputEditText>(R.id.text_input_text)

        MaterialAlertDialogBuilder(this@MainActivity)
            .setView(inputLayout)
            .setTitle(getString(R.string.dialog_morse_title))
            .setPositiveButton(android.R.string.ok, null)
            .create().apply {
                setOnShowListener {
                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val message = inputText.text.toString().trim()
                        if (message.isEmpty()) {
                            inputLayout.error = getString(R.string.dialog_morse_error_empty)
                        } else if (message.length > 50) {
                            inputLayout.error = getString(R.string.dialog_morse_error_length)
                        } else if (!Regex("[a-zA-Z0-9 ]+").matches(message)) {
                            inputLayout.error = getString(R.string.dialog_morse_error_characters)
                        } else {
                            dismiss()
                            binding.sosButton.hide()
                            binding.morseButton.disable()
                            switchMorseMode(true, getString(R.string.light_level_morse))
                            handleMorseCall(message)
                        }
                    }
                }
            }.show()
    }

    private fun switchMorseMode(activate: Boolean, message: String = "") {
        if (activate) {
            cameraManager.setTorchMode(cameraId, false)
            morseActivated = true
            binding.apply {
                binding.seekBar.setProgress(0)
                maxButton.hide()
                halfButton.hide()
                minButton.hide()
                seekBar.disable()
                @SuppressLint("SetTextI18n")
                binding.levelIndicator.text = message
            }
        } else {
            binding.apply {
                @SuppressLint("SetTextI18n")
                quickActionsView.text = getString(R.string.textview_quick_actions_title)
                maxButton.show()
                sosButton.enable()
                sosButton.show()
                morseButton.enable()
                morseButton.show()
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

    private fun CameraManager.sendLightLevel(level: Int) {
        if (currentLevel != level) {
            turnOnTorchWithStrengthLevel(cameraId, level)
        }
    }

    private fun isDimAllowed() = binding.maxButton.text == getString(R.string.button_max_maximum)

    private fun handleMorseCall(message: String) {
        lifecycleScope.launch {
            var lastLetter = Char.MIN_VALUE
            val handler = MorseHandler { letter, code, delay, on ->
                cameraManager.setTorchMode(cameraId, on)
                if (on) Vibrator.vibrate(vibrator, delay)

                if (lastLetter != letter) {
                    @SuppressLint("SetTextI18n")
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

    private fun doesDeviceHaveFlash(): Boolean {
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.icon_credits_action -> {
                showDialog(
                    getString(R.string.dialog_credits_title),
                    getString(R.string.dialog_credits_message),
                    { openUrl("https://flaticon.com", "Flaticon") },
                    getString(R.string.dialog_credits_button)
                )
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
