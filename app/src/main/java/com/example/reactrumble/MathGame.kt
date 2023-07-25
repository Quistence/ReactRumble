package com.example.reactrumble

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("ClickableViewAccessibility")
class MathGame : AppCompatActivity() {
    private val PREFS_FILENAME = "customizationsPreferences"
    private lateinit var preferences: SharedPreferences

    private val gameInstructions = "Tap if equations are correct!"

    private lateinit var equationTimer: CountDownTimer

    private var tapCount: Int = 0
    private var isGamePaused: Boolean = false

    companion object {
        //Can be configured from GameEngine
        private const val MAX_GAME_TIME = 60000L
        private const val DELAY_TIME = 2000L
        private val MAX_GAME_TAPS = GameManager.maxRoundsPerMiniGame
        private val COLOR_CORRECT = Color.parseColor("#C947D86B")
        private val COLOR_INCORRECT = Color.parseColor("#D34A4A")
        private val COLOR_DEFAULT = Color.parseColor("#A9A9C8")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        if(preferences.getBoolean("is_dark_mode", false))
            setContentView(R.layout.dark_math_minigame)
        else
            setContentView(R.layout.math_minigame)
        startGame()
    }

    private fun startGame() {
        val countdownTextP1: TextView = findViewById(R.id.countdownTextP1)
        val countdownTextP2: TextView = findViewById(R.id.countdownTextP2)
        val player1instructions: TextView = findViewById(R.id.player1_instructions)
        val player2instructions: TextView = findViewById(R.id.player2_instructions)

        countdownTextP1.text = gameInstructions
        countdownTextP2.text = gameInstructions
        player1instructions.text = gameInstructions
        player2instructions.text = gameInstructions

        //Set Initial Player Scores from previous games
        updateScoreText()

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            countdownTextP1.text = "3"
            countdownTextP2.text = "3"
            startInitialCountdown()
        }
    }

    private fun startInitialCountdown() {
        val countdownTextP1: TextView = findViewById(R.id.countdownTextP1)
        val countdownTextP2: TextView = findViewById(R.id.countdownTextP2)

        equationTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val count = millisUntilFinished / 1000
                if (count.toString() == "0") {
                    countdownTextP1.text = "GO!"
                    countdownTextP2.text = "GO!"
                } else {
                    countdownTextP1.text = count.toString()
                    countdownTextP2.text = count.toString()
                }
            }

            override fun onFinish() {
                countdownTextP1.text = ""
                countdownTextP2.text = ""
                startEquationGeneration()
            }
        }
        equationTimer.start()
    }

    private fun startEquationGeneration() {
        equationTimer = object : CountDownTimer(MAX_GAME_TIME, DELAY_TIME) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isGamePaused) {
                    generateEquation()
                }
            }

            override fun onFinish() {
                //Call Next Game or Game Over Screen
                GameManager.nextGame(this@MathGame)
            }
        }
        equationTimer.start()
    }

    private fun generateEquation() {
        val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
        val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
        val equationTextP1: TextView = findViewById(R.id.equationTextP1)
        val equationTextP2: TextView = findViewById(R.id.equationTextP2)

        val number1 = Random.nextInt(10)
        val number2 = Random.nextInt(10)
        val operator = if (Random.nextBoolean()) "+" else "-"
        val correctResult = if (operator == "+") number1 + number2 else number1 - number2

        val isCorrectEquation = Random.nextBoolean()
        val result = if (isCorrectEquation) correctResult else correctResult + Random.nextInt(-5, 5)

        equationTextP1.text = "$number1 $operator $number2 = $result"
        equationTextP2.text = "$number1 $operator $number2 = $result"

        player1Zone.setOnClickListener {
            handleTap(isCorrectEquation && result == correctResult, player1Zone, equationTextP1)
            checkGameOver(player1Zone, player2Zone)
        }

        player2Zone.setOnClickListener {
            handleTap(isCorrectEquation && result == correctResult, player2Zone, equationTextP2)
            checkGameOver(player1Zone, player2Zone)
        }
    }

    private fun handleTap(isCorrect: Boolean, playerZone: LinearLayout, equationText: TextView) {
        if (isCorrect) {
            playerZone.setBackgroundColor(COLOR_CORRECT)
            increaseScore(playerZone, equationText)
        } else {
            playerZone.setBackgroundColor(COLOR_INCORRECT)
            decreaseScore(playerZone, equationText)
        }
        disablePlayerZones()
    }

    private fun increaseScore(playerZone: LinearLayout, equationText: TextView) {
        when (playerZone.id) {
            R.id.player1_zone -> GameManager.playerOneScore++
            R.id.player2_zone -> GameManager.playerTwoScore++
        }
        equationText.text = "AWESOME JOB!"
        updateScoreText()
    }

    private fun decreaseScore(playerZone: LinearLayout, equationText: TextView) {
        when (playerZone.id) {
            R.id.player1_zone -> GameManager.playerOneScore--
            R.id.player2_zone -> GameManager.playerTwoScore--
        }
        equationText.text = "BOO! YOU SUCK!"
        updateScoreText()
    }

    private fun updateScoreText() {
        val player1DisplayScore: TextView = findViewById(R.id.player1_score)
        val player2DisplayScore: TextView = findViewById(R.id.player2_score)
        player1DisplayScore.text = "SCORE: ${GameManager.playerOneScore}"
        player2DisplayScore.text = "SCORE: ${GameManager.playerTwoScore}"
    }

    private fun disablePlayerZones() {
        val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
        val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
        player1Zone.isEnabled = false
        player2Zone.isEnabled = false
        isGamePaused = true
        GlobalScope.launch(Dispatchers.Main) {
            delay(DELAY_TIME)
            isGamePaused = false

            //For Touch Zones activation to be synced with equation generation resumption
            delay(700)

            player1Zone.isEnabled = true
            player1Zone.setBackgroundColor(COLOR_DEFAULT)
            player2Zone.isEnabled = true
            player2Zone.setBackgroundColor(COLOR_DEFAULT)
        }
    }

    private fun checkGameOver(player1Zone: LinearLayout, player2Zone: LinearLayout) {
        if (++tapCount >= MAX_GAME_TAPS || GameManager.playerOneScore >= GameManager.maxPointsPerMatch || GameManager.playerTwoScore >= GameManager.maxPointsPerMatch) {
            player1Zone.isClickable = false
            player2Zone.isClickable = false
            //Delay for players to check results of last round
            GlobalScope.launch(Dispatchers.Main) {
                delay(DELAY_TIME)
                equationTimer.cancel()
                equationTimer.onFinish()
            }
        }
    }

    override fun onBackPressed() {
        // Navigate to HomeActivity when back button is pressed
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

}