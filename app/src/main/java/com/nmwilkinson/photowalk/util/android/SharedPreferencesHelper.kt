package com.nmwilkinson.photowalk.util.android

import android.content.Context
import android.content.SharedPreferences

import java.util.HashSet

object SharedPreferencesHelper {

    private val PREFS_STORE_NAME = "hygf45yhgfghfg"

    fun clear(context: Context) {
        getPrefs(context).edit().clear().apply()
    }

    operator fun get(context: Context, key: String, defaultValue: String): String {
        return getPrefs(context).getString(key, defaultValue)
    }

    fun save(context: Context, key: String, values: List<String>) {
        getPrefs(context).edit().putStringSet(key, HashSet(values)).apply()
    }

    operator fun get(context: Context, key: String, defaultValues: List<String>): List<String> {
        return getPrefs(context).getStringSet(key, HashSet(defaultValues)).toList()
    }

    fun save(context: Context, key: String, value: String) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    operator fun get(context: Context, key: String, defaultValue: Long): Long {
        return getPrefs(context).getLong(key, defaultValue)
    }

    fun save(context: Context, key: String, value: Long) {
        getPrefs(context).edit().putLong(key, value).apply()
    }

    operator fun get(context: Context, key: String, defaultValue: Int): Int {
        return getPrefs(context).getInt(key, defaultValue)
    }

    fun save(context: Context, key: String, value: Int) {
        getPrefs(context).edit().putInt(key, value).apply()
    }

    operator fun get(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getPrefs(context).getBoolean(key, defaultValue)
    }

    fun save(context: Context, key: String, value: Boolean) {
        getPrefs(context).edit().putBoolean(key, value).apply()
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_STORE_NAME, Context.MODE_PRIVATE)
    }
}
