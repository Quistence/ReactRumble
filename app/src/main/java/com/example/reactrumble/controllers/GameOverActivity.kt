package com.example.reactrumble.controllers

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reactrumble.HomeActivity
import com.example.reactrumble.R

class GameOverActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_game_over)

		val p1Score = intent.getIntExtra("p1", 0)
		val p2Score = intent.getIntExtra("p2", 0)
		val win = intent.getIntExtra("winner", 0)
		val gameOverText: TextView = findViewById(R.id.gameOverText)
		val p1ScoreText: TextView = findViewById(R.id.p1ScoreText)
		val p2ScoreText: TextView = findViewById(R.id.p2ScoreText)

		p1ScoreText.text = p1Score.toString()
		p2ScoreText.text = p2Score.toString()

		if (win > 0) {
			gameOverText.text = "Player 1 Wins"
		} else if (win < 0) {
			gameOverText.text = "Player 2 Wins"
		} else {
			gameOverText.text = "Draw"
		}

		val playAgainButton: Button = findViewById(R.id.playAgainButton)
		playAgainButton.setOnClickListener {
			GameManager.startGame(this)
			this.finish()
		}
		val homeButton: Button = findViewById(R.id.homeButton)
		homeButton.setOnClickListener {
			val intent = Intent(this@GameOverActivity, HomeActivity::class.java)
			startActivity(intent)
			this.finish()
		}
	}

	override fun onBackPressed() {
		val intent = Intent(this, HomeActivity::class.java)
		startActivity(intent)
	}

}