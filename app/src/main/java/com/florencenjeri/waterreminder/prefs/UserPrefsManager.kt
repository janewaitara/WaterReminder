package com.florencenjeri.waterreminder.prefs

import android.content.SharedPreferences

class UserPrefsManager(private val prefs: SharedPreferences) {
    companion object {
        private const val USER_PREFS = "user-settings_config"
        private const val USER_ON_BOARD = "user-on-board_config"
        const val TIME_DELAY_PREFS = "user-settings_delay"
    }

    fun isUserSettingsConfigured() = prefs.getBoolean(USER_PREFS, false)

    fun isUserOnboarded() = prefs.getBoolean(USER_ON_BOARD, false)

    fun setUserSettingsConfig(isConfigured: Boolean) {
        prefs.edit().putBoolean(USER_PREFS, isConfigured).apply()
    }

    fun setUserOnboarded(isOnboarded: Boolean) {
        prefs.edit().putBoolean(USER_ON_BOARD, isOnboarded).apply()
    }

    fun storeRepeatIntervalTime(time: Long) {
        prefs.edit().putLong(TIME_DELAY_PREFS, time).apply()
    }
}
