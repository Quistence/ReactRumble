package com.example.reactrumble

import android.content.SharedPreferences
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
	private val PREFS_FILENAME = "customizationsPreferences"
	private lateinit var noOfRoundSpinner: AppCompatSpinner
	private lateinit var darkModeSwitch: SwitchCompat
	private lateinit var maxPointsPerGameRadioButtonGroup: RadioGroup
	private lateinit var btnSavePreferences: AppCompatButton
	private lateinit var preferences: SharedPreferences
	private lateinit var editor: SharedPreferences.Editor

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
		if(preferences.getBoolean("is_dark_mode", false))
			setContentView(R.layout.dark_activity_customize)
		else
			setContentView(R.layout.activity_customize)
		editor = preferences.edit()

		noOfRoundSpinner = findViewById(R.id.noOfRoundSpinner)
		darkModeSwitch = findViewById(R.id.darkModeSwitch)
		maxPointsPerGameRadioButtonGroup = findViewById(R.id.maxPointsPerGameRadioButtonGroup)
		btnSavePreferences = findViewById(R.id.btnSavePreferences)

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
			editor.putInt("no_of_rounds", selectedNoOfRounds)

			val isDarkMode = darkModeSwitch.isChecked
			editor.putBoolean("is_dark_mode", isDarkMode)

			val selectedMaxPointsPerGame =
				findViewById<RadioButton>(maxPointsPerGameRadioButtonGroup.checkedRadioButtonId)
			val maxPointsPerGame = selectedMaxPointsPerGame.text.toString().toInt()
			editor.putInt("max_points_per_game", maxPointsPerGame)

			editor.apply()
			Toast.makeText(this@CustomizeActivity, "Customization Saved", Toast.LENGTH_SHORT).show()
		}
		val savedNoOfRounds = preferences.getInt("no_of_rounds", 1)
		val savedDarkMode = preferences.getBoolean("is_dark_mode", false)
		val savedMaxPointsPerGame = preferences.getInt("max_points_per_game", 3)

		noOfRoundSpinner.setSelection(getIndex(noOfRoundSpinner, savedNoOfRounds.toString()))
		darkModeSwitch.isChecked = savedDarkMode

		// Set the appropriate RadioButton based on the savedMaxPointsPerGame value
		val radioButton: RadioButton = when (savedMaxPointsPerGame) {
			5 -> findViewById(R.id.fivePointRadioButton)
			10 -> findViewById(R.id.tenPointRadioButton)
			else -> findViewById(R.id.threePointRadioButton)
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