package com.example.reactrumble

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MathGame : AppCompatActivity() {

    // Initializing required variables for the mini game
    private var score = 0
    private lateinit var equationTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.math_minigame)

        startCountdown()
        setupEquationClickListener()
    }

    // Starts a countdown before the math equations start popping up
    private fun startCountdown() {

        var countdownText : TextView = findViewById(R.id.countdownText)
        equationTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val count = millisUntilFinished / 1000
                countdownText.text = count.toString()
            }

            override fun onFinish() {

                countdownText.text = "Go!"
                startEquationGeneration()
            }
        }
        equationTimer.start()
    }

    // Generate equations on a set timer before ending the game
    private fun startEquationGeneration() {

        equationTimer = object : CountDownTimer(100000, 1200) {
            override fun onTick(millisUntilFinished: Long) {
                generateEquation()
            }

            override fun onFinish() {
                // Game over logic
            }
        }
        equationTimer.start()
    }

    // Generates correct and incorrect simple mathematical equations
    private fun generateEquation() {

        var equationText : TextView = findViewById(R.id.equationText)
        val number1 = Random.nextInt(10)
        val number2 = Random.nextInt(10)
        val operator = if (Random.nextBoolean()) "+" else "-"
        val correctResult = if (operator == "+") number1 + number2 else number1 - number2

        val isCorrectEquation = Random.nextBoolean()

        val result = if (isCorrectEquation) {
            correctResult
        } else {
            // Generate an incorrect result
            var incorrectResult: Int
            do {
                incorrectResult =
                    correctResult + Random.nextInt(-5, 5) // Adjust the range as needed
            } while (incorrectResult == correctResult)
            incorrectResult
        }

        equationText.text = "$number1 $operator $number2 = $result"
        equationText.setOnClickListener {
            if ((isCorrectEquation && result == correctResult) || (!isCorrectEquation && result != correctResult)) {

                score++
                // Handle correct equation
            } else {

                score--
                // Handle incorrect equation
            }
        }
    }

    private fun setupEquationClickListener() {

        var equationText : TextView = findViewById(R.id.equationText)
        equationText.setOnClickListener {
            // Handle user tap when no equation is shown
            Toast.makeText(applicationContext, "You are Correct!", Toast.LENGTH_SHORT).show()
        }
    }
}
