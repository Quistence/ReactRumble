package com.example.reactrumble

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TestGameActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_game1)

        val p1Button : Button = findViewById(R.id.p1Score)
        val p2Button : Button = findViewById(R.id.p2Score)

        p1Button.setOnClickListener{
            GameManager.playerOneScore++
            GameManager.nextGame(this)
        }

        p2Button.setOnClickListener{
            GameManager.playerTwoScore++
            GameManager.nextGame(this)
        }

    }
}