package com.example.reactrumble.custompreferences

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.SwitchCompat
import com.example.reactrumble.HomeActivity
import com.example.reactrumble.R

class CustomizeActivity : AppCompatActivity() {
	private lateinit var noOfRoundSpinner: AppCompatSpinner
	private lateinit var darkModeSwitch: SwitchCompat
	private lateinit var maxMiniGamesPerMatchSpinner: AppCompatSpinner
	private lateinit var gameSpeedRadioButtonGroup: RadioGroup
	private lateinit var btnSavePreferences: AppCompatButton
	private lateinit var gamePreferences: GamePreferences
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_customize)


		noOfRoundSpinner = findViewById(R.id.noOfRoundSpinner)
		darkModeSwitch = findViewById(R.id.darkModeSwitch)
		maxMiniGamesPerMatchSpinner = findViewById(R.id.maxMiniGamesPerMatchSpinner)
		gameSpeedRadioButtonGroup = findViewById(R.id.gameSpeedRadioButtonGroup)
		btnSavePreferences = findViewById(R.id.btnSavePreferences)
		gamePreferences = GamePreferences.getInstance(applicationContext)

		ArrayAdapter.createFromResource(
			this@CustomizeActivity,
			R.array.no_of_rounds_array,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			noOfRoundSpinner.adapter = adapter
			maxMiniGamesPerMatchSpinner.adapter = adapter
		}

		btnSavePreferences.setOnClickListener {
			val selectedNoOfRounds = (noOfRoundSpinner.selectedItem as String).toInt()
			gamePreferences.saveNoOFRounds(selectedNoOfRounds)
			val selectedMaxMiniGamesPerMatch =
				(maxMiniGamesPerMatchSpinner.selectedItem as String).toInt()
			gamePreferences.saveMaxMiniGamesPerMatch(selectedMaxMiniGamesPerMatch)

			val selectedGameSpeed =
				findViewById<RadioButton>(gameSpeedRadioButtonGroup.checkedRadioButtonId)
			val gameSpeed = when (selectedGameSpeed.text.toString()) {
				"CRAZY!" -> 500L
				"SHORT" -> 1000L
				"LONG" -> 3000L
				else -> 2000L
			}
			gamePreferences.saveGameSpeed(gameSpeed)

			Toast.makeText(this@CustomizeActivity, "Customization Saved", Toast.LENGTH_SHORT).show()
			startActivity(Intent(this@CustomizeActivity, HomeActivity::class.java))
		}

		darkModeSwitch.setOnClickListener {
			val isDarkMode = darkModeSwitch.isChecked
			gamePreferences.saveDarkMode(isDarkMode)
			if (isDarkMode) {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			} else {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}
		}

		val savedNoOfRounds = gamePreferences.getNoOFRounds()
		val savedDarkMode = gamePreferences.getDarkMode()
		val savedMaxMiniGamesPerMatch = gamePreferences.getMaxMiniGamesPerMatch()
		val savedGameSpeed = gamePreferences.getGameSpeed()


		noOfRoundSpinner.setSelection(getIndex(noOfRoundSpinner, savedNoOfRounds.toString()))
		maxMiniGamesPerMatchSpinner.setSelection(
			getIndex(
				maxMiniGamesPerMatchSpinner,
				savedMaxMiniGamesPerMatch.toString()
			)
		)

		if (savedDarkMode != null) {
			darkModeSwitch.isChecked = savedDarkMode

			// Set the appropriate RadioButton based on the savedMaxPointsPerGame value
			val radioButton: RadioButton = when (savedGameSpeed) {
				500L -> findViewById(R.id.crazyRadioButton)
				1000L -> findViewById(R.id.shortRadioButton)
				3000L -> findViewById(R.id.longRadioButton)
				else -> findViewById(R.id.mediumRadioButton)
			}
			radioButton.isChecked = true

		}
	}
	private fun getIndex(spinner: AppCompatSpinner, value: String): Int {
		val adapter = spinner.adapter
		for (i in 0 until adapter.count) {
			if (adapter.getItem(i).toString() == value) {
				return i
			}
		}
		return 0
	}

}