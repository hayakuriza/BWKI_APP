package de.bwki.blumenidentifikator

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat


class SettingsFragment: PreferenceFragmentCompat(), MainActivity.GlobalMethods{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}