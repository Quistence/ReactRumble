package com.example.reactrumble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner

class CustomizeActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_customize)

		val noOfRoundSpinner: Spinner = findViewById(R.id.noOfRoundSpinner)
		ArrayAdapter.createFromResource(
			this@CustomizeActivity,
			R.array.no_of_rounds_array,
			android.R.layout.simple_spinner_item
		).also { adapter ->
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			noOfRoundSpinner.adapter = adapter
		}
	}
}