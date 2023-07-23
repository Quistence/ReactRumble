package com.example.reactrumble

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
	private val PREFS_FILENAME = "customizationsPreferences"
	private lateinit var preferences: SharedPreferences
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

		//if(preferences.getBoolean("is_dark_mode", false))
			//setContentView(R.layout.dark_activity_home)
		//else
			setContentView(R.layout.activity_home)

		val settingsButton: Button = findViewById(R.id.settingsButton)
		val customizeButton: Button = findViewById(R.id.customizeButton)
		val startGame: Button = findViewById(R.id.quickStartButton)

		startGame.setOnClickListener {
			startActivity(Intent(this@HomeActivity, MathGame::class.java))
		}

		settingsButton.setOnClickListener{
			startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))
		}

		customizeButton.setOnClickListener{
			startActivity(Intent(this@HomeActivity, CustomizeActivity::class.java))
		}
	}

	override fun onResume() {
		super.onResume()

		if(preferences.getBoolean("is_dark_mode", false))
			setContentView(R.layout.dark_activity_home)
		else
			setContentView(R.layout.activity_home)

		val settingsButton: Button = findViewById(R.id.settingsButton)
		val customizeButton: Button = findViewById(R.id.customizeButton)
		val startGame: Button = findViewById(R.id.quickStartButton)
		val games = arrayOf(MathGame::class.java, FlagsGame::class.java)

		startGame.setOnClickListener {
			//startActivity(Intent(this@HomeActivity, MathGame::class.java))
			GameManager.startGame(this, games)
		}

		settingsButton.setOnClickListener{
			startActivity(Intent(this@HomeActivity, SettingsActivity::class.java))
		}

		customizeButton.setOnClickListener{
			startActivity(Intent(this@HomeActivity, CustomizeActivity::class.java))
		}
	}
}