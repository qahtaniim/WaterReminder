package com.example.waterreminder
import androidx.datastore.preferences.core.stringPreferencesKey
import java.time.LocalDate
import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.booleanPreferencesKey

private val Context.dataStore by preferencesDataStore(name = "water_prefs")

object WaterPrefs {
    private val KEY_GOAL_ML = intPreferencesKey("goal_ml")
    private val KEY_CUP_ML = intPreferencesKey("cup_ml")
    private val KEY_TODAY_ML = intPreferencesKey("today_ml")
    private val KEY_LAST_DATE = stringPreferencesKey("last_date")
    private val KEY_REPEAT_MIN = intPreferencesKey("repeat_min")
    private val KEY_REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
    fun goalMlFlow(context: Context): Flow<Int> =
        context.dataStore.data.map { it[KEY_GOAL_ML] ?: 2500 }

    fun cupMlFlow(context: Context): Flow<Int> =
        context.dataStore.data.map { it[KEY_CUP_ML] ?: 250 }

    fun todayMlFlow(context: Context): Flow<Int> =
        context.dataStore.data.map { it[KEY_TODAY_ML] ?: 0 }

    suspend fun setGoalMl(context: Context, value: Int) {
        context.dataStore.edit { it[KEY_GOAL_ML] = value }
    }

    suspend fun setCupMl(context: Context, value: Int) {
        context.dataStore.edit { it[KEY_CUP_ML] = value }
    }

    suspend fun addDrink(context: Context, addMl: Int) {
        context.dataStore.edit {
            val current = it[KEY_TODAY_ML] ?: 0
            it[KEY_TODAY_ML] = current + addMl
        }
    }

    suspend fun resetToday(context: Context) {
        context.dataStore.edit { it[KEY_TODAY_ML] = 0 }

    }
    suspend fun ensureToday(context: Context) {
        val today = LocalDate.now().toString() // مثال: 2026-02-24

        context.dataStore.edit { prefs ->
            val last = prefs[KEY_LAST_DATE]

            if (last == null) {
                // أول مرة يشتغل التطبيق
                prefs[KEY_LAST_DATE] = today
            } else if (last != today) {
                // اليوم تغير -> صفّر عداد اليوم
                prefs[KEY_TODAY_ML] = 0
                prefs[KEY_LAST_DATE] = today
            }
        }
    }
    // ===== Reminder settings =====
    fun repeatMinFlow(context: Context): Flow<Int> =
        context.dataStore.data.map { it[KEY_REPEAT_MIN] ?: 60 }

    fun reminderEnabledFlow(context: Context): Flow<Boolean> =
        context.dataStore.data.map { it[KEY_REMINDER_ENABLED] ?: false }

    suspend fun setRepeatMin(context: Context, min: Int) {
        context.dataStore.edit { it[KEY_REPEAT_MIN] = min }
    }

    suspend fun setReminderEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[KEY_REMINDER_ENABLED] = enabled }
    }
}