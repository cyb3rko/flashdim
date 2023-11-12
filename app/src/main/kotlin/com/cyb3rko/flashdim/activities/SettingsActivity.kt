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

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.cyb3rko.flashdim.R
import com.cyb3rko.flashdim.databinding.ActivitySettingsBinding
import com.cyb3rko.flashdim.modals.AccessibilityInfoDialog
import com.cyb3rko.flashdim.utils.Safe
import com.cyb3rko.flashdim.utils.Vibrator
import com.google.android.material.R as MaterialR
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider

internal class SettingsActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .replace(binding.settingsContainer.id, SettingsFragment())
            .commit()

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        window.setDecorFitsSystemWindows( false )


        binding.appBarLayout.setOnApplyWindowInsetsListener { view, windowInsets ->
            val insets = windowInsets.getInsets( WindowInsets.Type.statusBars() )
            view.updatePadding( top = insets.top )
            windowInsets
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            Safe.MORSE_VIBRATION, Safe.BUTTON_VIBRATION -> {
                showRestartDialog()
            }
        }
    }

    private fun showRestartDialog() {
        MaterialAlertDialogBuilder(
            this,
            MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
            .setIcon(android.R.drawable.stat_notify_sync)
            .setTitle(getString(R.string.dialog_restart_title))
            .setMessage(getString(R.string.dialog_restart_message))
            .setPositiveButton(getString(R.string.dialog_restart_positive_button)) { _, _ ->
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                val componentName = intent!!.component
                val mainIntent = Intent.makeRestartActivityTask(componentName)
                startActivity(mainIntent)
                Runtime.getRuntime().exit(0)
            }
            .setNegativeButton(getString(R.string.dialog_restart_negative_button), null)
            .show()
    }

    internal class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var myContext: Context

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            myContext = requireContext()
            setPreferencesFromResource(R.xml.preferences, rootKey)

            findPreference<Preference>(Safe.INITIAL_LEVEL)?.apply {
                if (!Safe.getBoolean(Safe.MULTILEVEL, false)) {
                    isEnabled = false
                    return@apply
                }

                val initialLevel = Safe.getInt(Safe.INITIAL_LEVEL, 1)
                val summaryString = getString(R.string.preference_item_initial_level_summary)
                summary = "$summaryString: $initialLevel"

                setOnPreferenceClickListener { preference ->
                    val currentInitialLevel = Safe.getInt(Safe.INITIAL_LEVEL, 1)
                    val withVibration = Safe.getBoolean(Safe.BUTTON_VIBRATION, true)
                    val content = LinearLayout(myContext).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(75, 0, 75, 0)
                    }
                    val levelView = TextView(myContext).apply {
                        textSize = 18f
                        setPadding(24, 24, 24, 50)
                        gravity = Gravity.CENTER_HORIZONTAL
                        text = String.format(
                            getString(R.string.preference_item_initial_level_dialog_message),
                            currentInitialLevel,
                            currentInitialLevel
                        )
                    }
                    content.addView(levelView)
                    val slider = Slider(myContext).apply {
                        valueFrom = 1F
                        valueTo = Safe.getInt(Safe.MAX_LEVEL, 0).toFloat()
                        value = currentInitialLevel.toFloat()
                        stepSize = 1F
                        addOnChangeListener { _, value, _ ->
                            if (withVibration) Vibrator.vibrateTick()
                            levelView.text = String.format(
                                getString(R.string.preference_item_initial_level_dialog_message),
                                value.toInt(),
                                currentInitialLevel
                            )
                        }
                    }
                    content.addView(slider)
                    showInitialLevelDialog(content) {
                        val value = slider.value.toInt()
                        Safe.writeInt(Safe.INITIAL_LEVEL, value)

                        val summary = getString(R.string.preference_item_initial_level_summary)
                        preference.summary = "$summary: $value"
                    }
                    true
                }
            }
            findPreference<Preference>("volume_buttons")?.setOnPreferenceClickListener {
                AccessibilityInfoDialog.show(myContext)
                true
            }
        }

        private fun showInitialLevelDialog(content: View, onSave: () -> Unit) {
            MaterialAlertDialogBuilder(
                requireContext(),
                MaterialR.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
            )
                .setIcon(R.drawable._ic_level)
                .setTitle(getString(R.string.preference_item_initial_level_dialog_title))
                .setView(content)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    onSave()
                }
                .setNegativeButton(
                    getString(R.string.preference_item_initial_level_dialog_negative_button),
                    null
                )
                .show()
        }
    }
}
