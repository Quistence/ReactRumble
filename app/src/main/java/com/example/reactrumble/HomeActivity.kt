package com.example.reactrumble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class HomeActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
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
}