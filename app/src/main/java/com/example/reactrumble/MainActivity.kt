package com.example.reactrumble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		val activityArr = arrayOf(TestGameActivity1::class.java, TestGameActivity2::class.java)
		GameManager.initialize(activityArr, 5)

		val button: Button = findViewById(R.id.startButton)
		button.setOnClickListener{
			GameManager.startGame(this)
		}
	}

}
