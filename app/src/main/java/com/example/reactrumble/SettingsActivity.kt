package com.example.reactrumble

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class SettingsActivity : AppCompatActivity() {
	private val PREFS_FILENAME = "customizationsPreferences"
	private lateinit var preferences: SharedPreferences
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

		if(preferences.getBoolean("is_dark_mode", false))
			setContentView(R.layout.dark_activity_settings)
		else
			setContentView(R.layout.activity_settings)

		val languageSpinner: Spinner = findViewById(R.id.languageSpinner)
		ArrayAdapter.createFromResource(
			this@SettingsActivity,
			R.array.languages_array,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			languageSpinner.adapter = adapter
		}

	}
}