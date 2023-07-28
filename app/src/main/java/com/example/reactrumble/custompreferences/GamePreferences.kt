package com.example.reactrumble.custompreferences

import android.content.Context
import android.content.SharedPreferences

class GamePreferences private constructor(context: Context) {
	companion object {
		private const val PREFS_NAME = "customizationsPreferences"
		private var sharedPreferences: SharedPreferences? = null

		@Volatile
		private var instance: GamePreferences? = null

		// Singleton pattern to get the instance of GamePreferences
		fun getInstance(context: Context): GamePreferences {
			return instance ?: synchronized(this) {
				instance ?: GamePreferences(context.applicationContext).also { instance = it }
			}
		}
	}

	init {
		// Initialize SharedPreferences if not already initialized
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
		}
	}

	// Function to save the selected number of rounds
	fun saveNoOFRounds(value: Int) {
		sharedPreferences?.edit()?.putInt("no_of_rounds", value)?.apply()
	}

	// Function to get the saved number of rounds or return the default value (3)
	fun getNoOFRounds(): Int? {
		return sharedPreferences?.getInt("no_of_rounds", 3)
	}

	// Function to save the selected maximum number of mini-games per match
	fun saveMaxMiniGamesPerMatch(value: Int) {
		sharedPreferences?.edit()?.putInt("max_mini_games_per_match", value)?.apply()
	}

	// Function to get the saved maximum number of mini-games per match or return the default value (3)
	fun getMaxMiniGamesPerMatch(): Int? {
		return sharedPreferences?.getInt("max_mini_games_per_match", 3)
	}

	// Function to save the selected dark mode preference
	fun saveDarkMode(value: Boolean) {
		sharedPreferences?.edit()?.putBoolean("is_dark_mode", value)?.apply()
	}

	// Function to get the saved dark mode preference or return the default value (false)
	fun getDarkMode(): Boolean? {
		return sharedPreferences?.getBoolean("is_dark_mode", false)
	}

	// Function to save the selected game speed
	fun saveGameSpeed(value: Long) {
		sharedPreferences?.edit()?.putLong("game_speed", value)?.apply()
	}

	// Function to get the saved game speed or return the default value (2000L)
	fun getGameSpeed(): Long? {
		return sharedPreferences?.getLong("game_speed", 2000L)
	}
}
