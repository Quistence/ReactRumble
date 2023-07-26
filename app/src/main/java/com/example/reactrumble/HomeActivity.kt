package com.example.reactrumble

import android.animation.ValueAnimator
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.example.reactrumble.controllers.GameManager
import com.example.reactrumble.custompreferences.CustomizeActivity
import com.example.reactrumble.minigames.ColorsGame
import com.example.reactrumble.minigames.FlagsGame
import com.example.reactrumble.minigames.MathGame

class HomeActivity : AppCompatActivity() {
	private val PREFS_FILENAME = "customizationsPreferences"
	private lateinit var preferences: SharedPreferences
	private lateinit var gamePreferences: GamePreferences
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
		gamePreferences = GamePreferences.getInstance(applicationContext)
		if(gamePreferences.getDarkMode()==true) {
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
		}
		else{
			AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		}

		setContentView(R.layout.activity_home)

		val customizeButton: Button = findViewById(R.id.customizeButton)

		customizeButton.setOnClickListener{
			startActivity(Intent(this@HomeActivity, CustomizeActivity::class.java))
		}
	}

	override fun onResume() {
		super.onResume()

		val customizeButton: Button = findViewById(R.id.customizeButton)
		val startGame: Button = findViewById(R.id.quickStartButton)
		val games = arrayOf(MathGame::class.java, FlagsGame::class.java, ColorsGame::class.java)

		// Find the React Rumble icon
		val iconImage: ImageView = findViewById(R.id.provinceImage)

		// Start the animation
		startBounceAnimation(iconImage)

		startGame.setOnClickListener {
			//startActivity(Intent(this@HomeActivity, MathGame::class.java))
			GameManager.startGame(this, games)
		}
		customizeButton.setOnClickListener{
			startActivity(Intent(this@HomeActivity, CustomizeActivity::class.java))
		}
	}

	private fun startBounceAnimation(iconImage: ImageView) {
		// Set up the animator to scale the ImageView
		val animator = ValueAnimator.ofFloat(1f, 1.5f, 1f)
		animator.duration = 2000 // Duration of the animation in milliseconds
		animator.addUpdateListener { animation ->
			val scale = animation.animatedValue as Float
			iconImage.scaleX = scale
			iconImage.scaleY = scale
		}

		// Set the repeat count to INFINITE for continuous animation
		animator.repeatCount = ValueAnimator.INFINITE

		// Start the animator
		animator.start()
	}
}