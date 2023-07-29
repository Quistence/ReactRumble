package com.example.reactrumble.custompreferences

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

		// Set up ArrayAdapter for spinners to display dropdown items from the given array
		ArrayAdapter.createFromResource(
			this@CustomizeActivity,
			R.array.no_of_rounds_array,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			noOfRoundSpinner.adapter = adapter
			maxMiniGamesPerMatchSpinner.adapter = adapter
		}

		// Save the user's preferences when the save button is clicked
		btnSavePreferences.setOnClickListener {
			// Save selected number of rounds
			val selectedNoOfRounds = (noOfRoundSpinner.selectedItem as String).toInt()
			gamePreferences.saveNoOFRounds(selectedNoOfRounds)

			// Save selected maximum number of mini-games per match
			val selectedMaxMiniGamesPerMatch =
				(maxMiniGamesPerMatchSpinner.selectedItem as String).toInt()
			gamePreferences.saveMaxMiniGamesPerMatch(selectedMaxMiniGamesPerMatch)

			// Save selected game speed
			val selectedGameSpeed =
				findViewById<RadioButton>(gameSpeedRadioButtonGroup.checkedRadioButtonId)
			val gameSpeed = when (selectedGameSpeed.text.toString()) {
				"CRAZY!" -> 500L
				"SHORT" -> 1000L
				"LONG" -> 3000L
				else -> 2000L
			}
			gamePreferences.saveGameSpeed(gameSpeed)

			// Display a toast message indicating that the customization is saved
			Toast.makeText(this@CustomizeActivity, "Customization Saved", Toast.LENGTH_SHORT).show()

			// Start the HomeActivity after saving preferences
			startActivity(Intent(this@CustomizeActivity, HomeActivity::class.java))
		}

		// Set up the listener for the dark mode switch
		darkModeSwitch.setOnClickListener {
			val isDarkMode = darkModeSwitch.isChecked
			// Save the dark mode preference
			gamePreferences.saveDarkMode(isDarkMode)
			// Set the AppCompatDelegate to use the selected night mode (dark/light)
			if (isDarkMode) {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
			} else {
				AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
			}
		}

		// Retrieve and apply previously saved preferences to UI elements
		val savedNoOfRounds = gamePreferences.getNoOFRounds()
		val savedDarkMode = gamePreferences.getDarkMode()
		val savedMaxMiniGamesPerMatch = gamePreferences.getMaxMiniGamesPerMatch()
		val savedGameSpeed = gamePreferences.getGameSpeed()

		// Set the selected values in the spinners based on the saved preferences
		noOfRoundSpinner.setSelection(getIndex(noOfRoundSpinner, savedNoOfRounds.toString()))
		maxMiniGamesPerMatchSpinner.setSelection(
			getIndex(
				maxMiniGamesPerMatchSpinner,
				savedMaxMiniGamesPerMatch.toString()
			)
		)

		// If dark mode was previously enabled, set the switch to ON and set the radio button
		// for the saved game speed to checked
		if (savedDarkMode != null) {
			darkModeSwitch.isChecked = savedDarkMode

			val radioButton: RadioButton = when (savedGameSpeed) {
				500L -> findViewById(R.id.crazyRadioButton)
				1000L -> findViewById(R.id.shortRadioButton)
				3000L -> findViewById(R.id.longRadioButton)
				else -> findViewById(R.id.mediumRadioButton)
			}
			radioButton.isChecked = true
		}
	}

	// Function to find the index of a value in the spinner's adapter
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
