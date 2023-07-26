package com.example.reactrumble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.SwitchCompat

class CustomizeActivity : AppCompatActivity() {
	private lateinit var noOfRoundSpinner: AppCompatSpinner
	private lateinit var randomizeSwitch: SwitchCompat
	private lateinit var maxMiniGamesPerMatchSpinner: AppCompatSpinner
	private lateinit var gameSpeedRadioButtonGroup: RadioGroup
	private lateinit var btnSavePreferences: AppCompatButton
	private lateinit var gamePreferences: GamePreferences
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_customize)


		noOfRoundSpinner = findViewById(R.id.noOfRoundSpinner)
		randomizeSwitch = findViewById(R.id.randomizeSwitch)
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
			val isRandomized = randomizeSwitch.isChecked
			gamePreferences.saveGameRandomization(isRandomized)
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
		}
		val savedNoOfRounds = gamePreferences.getNoOFRounds()
		val savedRandomized = gamePreferences.getGameRandomization()
		val savedMaxMiniGamesPerMatch = gamePreferences.getMaxMiniGamesPerMatch()
		val savedGameSpeed = gamePreferences.getGameSpeed()


		noOfRoundSpinner.setSelection(getIndex(noOfRoundSpinner, savedNoOfRounds.toString()))
		maxMiniGamesPerMatchSpinner.setSelection(
			getIndex(
				maxMiniGamesPerMatchSpinner,
				savedMaxMiniGamesPerMatch.toString()
			)
		)


		// Set the appropriate RadioButton based on the savedMaxPointsPerGame value
		val radioButton: RadioButton = when (savedGameSpeed) {
			500L -> findViewById(R.id.crazyRadioButton)
			1000L -> findViewById(R.id.shortRadioButton)
			3000L -> findViewById(R.id.longRadioButton)
			else -> findViewById(R.id.mediumRadioButton)
		}
		radioButton.isChecked = true


		if (savedRandomized != null) {
			randomizeSwitch.isChecked = savedRandomized
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