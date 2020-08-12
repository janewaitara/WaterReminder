package com.florencenjeri.waterreminder.prefs

import android.content.SharedPreferences

class UserPrefsManager(private val prefs: SharedPreferences) {
    companion object {
        private const val USER_PREFS = "user-settings_config"
    }

    fun isUserSettingsConfigured() = prefs.getBoolean(USER_PREFS, false)


    fun setUserSettingsConfig(isConfigured: Boolean) {
        prefs.edit().putBoolean(USER_PREFS, isConfigured).apply()
    }
}
