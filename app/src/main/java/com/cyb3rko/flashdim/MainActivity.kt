package com.cyb3rko.flashdim

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cyb3rko.flashdim.databinding.ActivityMainBinding
import com.cyb3rko.flashdim.seekbar.SeekBarChangeListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.math.roundToInt
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val cameraId by lazy { cameraManager.cameraIdList[0] }
    private val cameraManager by lazy { getSystemService(Context.CAMERA_SERVICE) as CameraManager }
    private var currentLevel = -1
    private var maxLevel = -1

    private var sosActivated = false
    private val sosHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!doesDeviceHaveFlash()) {
            setContentView(View(this))
            MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle("Device not supported")
                .setMessage("This device does not have a flash light.")
                .setPositiveButton("Close") { _, _ ->
                    exitProcess(0)
                }
                .show()
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
                maxProgress = maxLevel.toFloat()
                onProgressChanged = object : SeekBarChangeListener {
                    override fun onProgressChanged(progress: Float) {
                        val progressInt = progress.roundToInt()
                        if (progressInt > 0) {
                            if (progressInt <= maxLevel) {
                                cameraManager.sendLightLevel(progressInt)
                                updateLightLevelView(progressInt)
                                currentLevel = progressInt
                            } else {
                                cameraManager.sendLightLevel(maxLevel)
                                updateLightLevelView(maxLevel)
                                currentLevel = progressInt
                            }
                        } else if (progressInt == 0) {
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
                sosActivated = true
                cameraManager.setTorchMode(cameraId, false)
                seekBar.setProgress(0)
                updateLightLevelView(0, " (SOS)")
                maxButton.hide()
                halfButton.hide()
                minButton.hide()
                seekBar.disable()
                handleSosCall()
            }
            maxButton.setOnClickListener {
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
                updateLightLevelView(maxLevel / 2)
                cameraManager.sendLightLevel(maxLevel / 2)
                currentLevel = maxLevel / 2
                seekBar.setProgress(maxLevel / 2)
            }
            minButton.setOnClickListener {
                updateLightLevelView(1)
                cameraManager.sendLightLevel(1)
                currentLevel = 1
                seekBar.setProgress(1)
            }
            offButton.setOnClickListener {
                val dimAllowed = isDimAllowed()
                if (sosActivated) {
                    sosHandler.removeCallbacksAndMessages(null)
                    sosActivated = false
                    binding.apply {
                        maxButton.show()
                        sosButton.enable()
                        if (dimAllowed) {
                            halfButton.show()
                            minButton.show()
                            seekBar.enable()
                        }
                    }
                }
                if (dimAllowed) {
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
        }
    }

    @SuppressLint("SetTextI18n")
    private fun switchToSimpleMode() {
        binding.apply {
            buttonContainer.setPadding(0, 24, 64, 24)
            maxButton.text = "On"
            halfButton.hide()
            minButton.hide()
            seekBar.hide()
            levelIndicatorDesc.makeInvisible()
            levelIndicator.makeInvisible()
            errorView.text = "This evice only supports 1 light level.\nDim feature deactivated."
            errorView.show()
            quickActionsView.hide()
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

    private fun isDimAllowed() = binding.maxButton.text == "Maximum"

    private fun handleSosCall() {
        if (!sosActivated) return
        val t = 250L
        val runnableOn = Runnable {
            cameraManager.setTorchMode(cameraId, true)
            updateLightLevelView(maxLevel, " (SOS)")
            binding.seekBar.setProgress(maxLevel)
        }
        val runnableOff = Runnable {
            cameraManager.setTorchMode(cameraId, false)
            updateLightLevelView(0, " (SOS)")
            binding.seekBar.setProgress(0)
        }
        val runnableFinishRound = Runnable {
            handleSosCall()
        }

        handleSosS(runnableOn, runnableOff, t, 4 * t)
        handleSosO(runnableOn, runnableOff, t, 12 * t)
        handleSosS(runnableOn, runnableOff, t, 26 * t)
        finishSosRound(runnableFinishRound, 34 * t)
    }

    private fun handleSosS(on: Runnable, off: Runnable, t: Long, offset: Long) {
        sosHandler.postDelayed(on, 3 * t + offset)
        sosHandler.postDelayed(off, 4 * t + offset)
        sosHandler.postDelayed(on, 5 * t + offset)
        sosHandler.postDelayed(off, 6 * t + offset)
        sosHandler.postDelayed(on, 7 * t + offset)
        sosHandler.postDelayed(off, 8 * t + offset)
    }

    private fun handleSosO(on: Runnable, off: Runnable, t: Long, offset: Long) {
        sosHandler.postDelayed(on, 3 * t + offset)
        sosHandler.postDelayed(off, 6 * t + offset)
        sosHandler.postDelayed(on, 7 * t + offset)
        sosHandler.postDelayed(off, 10 * t + offset)
        sosHandler.postDelayed(on, 11 * t + offset)
        sosHandler.postDelayed(off, 14 * t + offset)
    }

    private fun finishSosRound(r: Runnable, offset: Long) {
        sosHandler.postDelayed(r, offset)
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
                MaterialAlertDialogBuilder(this)
                    .setTitle("Icon Credits")
                    .setMessage("Flashlight icon created by Freepik - Flaticon\n\n" +
                            "Knob icon created by Debi Alpa Nugraha - Flaticon")
                    .setPositiveButton("Open Flaticon") { _, _ ->
                        openURL("https://flaticon.com")
                    }
                    .show()
                return true
            }
            R.id.github_action -> {
                openURL("https://github.com/cyb3rko/flashdim")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun View.disable() {
        this.isEnabled = false
    }

    private fun View.enable() {
        this.isEnabled = true
    }

    private fun View.hide() {
        this.visibility = View.GONE
    }

    private fun View.makeInvisible() {
        this.visibility = View.INVISIBLE
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }

    private fun openURL(url: String) {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            val clip = ClipData.newPlainText("Flaticon", url)
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(clip)
            Toast.makeText(
                this,
                "Opening URL failed, copied URL instead",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
