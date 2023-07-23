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

	fun saveMaxPoint(value: Int) {
		sharedPreferences?.edit()?.putInt("max_points_per_game", value)?.apply()
	}

	fun getMaxPoint(): Int? {
		return sharedPreferences?.getInt("max_points_per_game", 3)
	}

	fun saveGameRandomization(value: Boolean) {
		sharedPreferences?.edit()?.putBoolean("is_randomized", value)?.apply()
	}

	fun getGameRandomization(): Boolean? {
		return sharedPreferences?.getBoolean("is_randomized", false)
	}
}