package com.cyb3rko.flashdim

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.cyb3rko.flashdim.databinding.ActivitySettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            Safe.MORSE_VIBRATION, Safe.BUTTON_VIBRATION -> {
                MaterialAlertDialogBuilder(
                    this,
                    com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
                )
                    .setIcon(ResourcesCompat.getDrawable(
                        resources,
                        android.R.drawable.stat_notify_sync,
                        theme
                    ))
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
        }
    }

    internal class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        }
    }
}
