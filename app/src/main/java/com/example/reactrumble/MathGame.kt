package com.example.reactrumble

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MathGame : AppCompatActivity() {

    // Initializing placeholder score variable for testing and equationTimer for the mini game
    private var score = 0
    private lateinit var equationTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.math_minigame)

        startGame()

        setupEquationClickListener() // Placeholder tester method
    }

    // This method is run to start the game, it displays the game instructions and then runs the
    // startCountdown() which in turn runs the startEquationGeneration() method
    private fun startGame() {

        var countdownTextP1 : TextView = findViewById(R.id.countdownTextP1)
        var countdownTextP2 : TextView = findViewById(R.id.countdownTextP2)

        countdownTextP1.text = "Tap correct equations!"
        countdownTextP2.text = "Tap correct equations!"

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000) // Display the game instructions for 3 seconds
            countdownTextP1.text = "3"
            countdownTextP2.text = "3"

            startCountdown()
        }
    }

    // Starts a countdown before the math equations start popping up
    private fun startCountdown() {

        var countdownTextP1 : TextView = findViewById(R.id.countdownTextP1)
        var countdownTextP2 : TextView = findViewById(R.id.countdownTextP2)

        equationTimer = object : CountDownTimer(4000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val count = millisUntilFinished / 1000
                countdownTextP1.text = count.toString()
                countdownTextP2.text = count.toString()
            }

            override fun onFinish() {

                countdownTextP1.text = "Go!"
                countdownTextP2.text = "Go!"

                // The "Go!" vanishes after 1 second of appearing
                GlobalScope.launch(Dispatchers.Main) {
                    delay(1000)
                    countdownTextP1.text = ""
                    countdownTextP2.text = ""

                    // Start generating the equations
                    startEquationGeneration()
                }
            }
        }

        equationTimer.start()
    }

    // Generate equations on a set timer before ending the game
    private fun startEquationGeneration() {

        // Adjust the time for the duration of the game down here
        equationTimer = object : CountDownTimer(100000, 1200) {
            override fun onTick(millisUntilFinished: Long) {
                generateEquation()
            }

            override fun onFinish() {
                // Game over logic should be placed here
            }
        }
        equationTimer.start()
    }

    // Generates correct and incorrect simple mathematical equations
    private fun generateEquation() {

        var equationTextP1 : TextView = findViewById(R.id.equationTextP1)
        var equationTextP2 : TextView = findViewById(R.id.equationTextP2)

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

        // Set the equations to player 1 and player 2 TextViews
        equationTextP1.text = "$number1 $operator $number2 = $result"
        equationTextP2.text = "$number1 $operator $number2 = $result"

        // This is a placeholder click listener that checks whether the tapped equation is correct or not
        equationTextP1.setOnClickListener {
            if ((isCorrectEquation && result == correctResult) || (!isCorrectEquation && result != correctResult)) {

                score++
                // Handle correct equation
            } else {

                score--
                // Handle incorrect equation
            }
        }
    }

    // Placeholder tester method for scoring
    // What happens when player 1 presses their equation
    private fun setupEquationClickListener() {

        var equationTextP1 : TextView = findViewById(R.id.equationTextP1)
        equationTextP1.setOnClickListener {
            // Handle user tap when no equation is shown
            Toast.makeText(applicationContext, "You are Correct!", Toast.LENGTH_SHORT).show()
        }
    }
}
