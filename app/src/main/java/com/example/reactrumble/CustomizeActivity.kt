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
	private lateinit var maxMiniGamesPerMatchRadioGroup: RadioGroup
	private lateinit var btnSavePreferences: AppCompatButton
	private lateinit var gamePreferences: GamePreferences
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_customize)


		noOfRoundSpinner = findViewById(R.id.noOfRoundSpinner)
		randomizeSwitch = findViewById(R.id.randomizeSwitch)
		maxMiniGamesPerMatchRadioGroup = findViewById(R.id.maxMiniGamesPerMatch)
		btnSavePreferences = findViewById(R.id.btnSavePreferences)
		gamePreferences = GamePreferences.getInstance(applicationContext)

		ArrayAdapter.createFromResource(
			this@CustomizeActivity,
			R.array.no_of_rounds_array,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			noOfRoundSpinner.adapter = adapter
		}

		btnSavePreferences.setOnClickListener {
			val selectedNoOfRounds = (noOfRoundSpinner.selectedItem as String).toInt()
			gamePreferences.saveNoOFRounds(selectedNoOfRounds)
			val isRandomized = randomizeSwitch.isChecked
			gamePreferences.saveGameRandomization(isRandomized)

			val selectedMaxMiniGamesPerMatch =
				findViewById<RadioButton>(maxMiniGamesPerMatchRadioGroup.checkedRadioButtonId)
			val maxMiniGamesPerMatch = selectedMaxMiniGamesPerMatch.text.toString().toInt()
			gamePreferences.saveMaxMiniGamesPerMatch(maxMiniGamesPerMatch)

			Toast.makeText(this@CustomizeActivity, "Customization Saved", Toast.LENGTH_SHORT).show()
		}
		val savedNoOfRounds = gamePreferences.getNoOFRounds()
		val savedRandomized = gamePreferences.getGameRandomization()
		val savedMaxPointsPerGame = gamePreferences.getMaxMiniGamesPerMatch()

		noOfRoundSpinner.setSelection(getIndex(noOfRoundSpinner, savedNoOfRounds.toString()))

		if (savedRandomized != null) {
			randomizeSwitch.isChecked = savedRandomized
		}

		// Set the appropriate RadioButton based on the savedMaxPointsPerGame value
		val radioButton: RadioButton = when (savedMaxPointsPerGame) {
			5 -> findViewById(R.id.threePointRadioButton)
			10 -> findViewById(R.id.fivePointRadioButton)
			else -> findViewById(R.id.tenPointRadioButton)
		}
		radioButton.isChecked = true
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