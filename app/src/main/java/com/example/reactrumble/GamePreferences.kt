package com.example.reactrumble

import android.content.Context
import android.content.SharedPreferences

class GamePreferences private constructor(context: Context){
//	private val sharedPreferences: SharedPreferences =
//		context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

	companion object {
		private const val PREFS_NAME = "customizationsPreferences"
		private var sharedPreferences: SharedPreferences? = null

		@Volatile
		private var instance: GamePreferences? = null

		fun getInstance(context: Context): GamePreferences {
			return instance ?: synchronized(this) {
				instance ?: GamePreferences(context.applicationContext).also { instance = it }
			}
		}
	}
	init {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
		}
	}

	fun saveNoOFRounds(value: Int) {
		sharedPreferences?.edit()?.putInt("no_of_rounds", value)?.apply()
	}

	fun getNoOFRounds(): Int? {
		return sharedPreferences?.getInt("no_of_rounds", 3)
	}

	fun saveMaxMiniGamesPerMatch(value: Int) {
		sharedPreferences?.edit()?.putInt("max_mini_games_per_match", value)?.apply()
	}

	fun getMaxMiniGamesPerMatch(): Int? {
		return sharedPreferences?.getInt("max_mini_games_per_match", 3)
	}

	fun saveGameRandomization(value: Boolean) {
		sharedPreferences?.edit()?.putBoolean("is_randomized", value)?.apply()
	}

	fun getGameRandomization(): Boolean? {
		return sharedPreferences?.getBoolean("is_randomized", false)
	}

	fun saveGameSpeed(value: Long){
		sharedPreferences?.edit()?.putLong("game_speed", value)?.apply()
	}

	fun getGameSpeed(): Long? {
		return sharedPreferences?.getLong("game_speed", 2000L)
	}
}