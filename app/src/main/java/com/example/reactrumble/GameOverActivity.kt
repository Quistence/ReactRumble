package com.example.reactrumble

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GameOverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        val p1Score = intent.getIntExtra("p1", 0)
        val p2Score = intent.getIntExtra("p2", 0)
        val p1Win = intent.getBooleanExtra("winner", true)
        val gameOverText : TextView = findViewById(R.id.gameOverText)
        val p1ScoreText : TextView = findViewById(R.id.p1ScoreText)
        val p2ScoreText :TextView = findViewById(R.id.p2ScoreText)

        p1ScoreText.text = p1Score.toString()
        p2ScoreText.text = p2Score.toString()

        if(p1Win){
            gameOverText.text = "Player 1 Wins"
        } else {
            gameOverText.text = "Player 2 Wins"
        }

        val homeButton : Button = findViewById(R.id.homeButton)
        homeButton.setOnClickListener{
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }
    }
}