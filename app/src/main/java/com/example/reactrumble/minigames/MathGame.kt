package com.example.reactrumble.minigames

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reactrumble.HomeActivity
import com.example.reactrumble.R
import com.example.reactrumble.controllers.GameManager
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
        private var maxGameTaps = GameManager.maxRoundsPerMiniGame
        private val COLOR_CORRECT = Color.parseColor("#C947D86B")
        private val COLOR_INCORRECT = Color.parseColor("#D34A4A")
        private val COLOR_DEFAULT = Color.parseColor("#A9A9C8")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)
        maxGameTaps = GameManager.maxRoundsPerMiniGame
        super.onCreate(savedInstanceState)
        preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

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
            delay(GameManager.gameDelayTime)
            countdownTextP1.text = ""
            countdownTextP2.text = ""
            startEquationGeneration()
        }
    }

    private fun startEquationGeneration() {
        equationTimer = object : CountDownTimer(MAX_GAME_TIME, GameManager.gameDelayTime) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isGamePaused) {
                    val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
                    val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
                    player1Zone.isEnabled = true
                    player1Zone.setBackgroundColor(COLOR_DEFAULT)
                    player2Zone.isEnabled = true
                    player2Zone.setBackgroundColor(COLOR_DEFAULT)
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

        var incorrectResultOffset = Random.nextInt(-5, 5)
        while (incorrectResultOffset == 0) {
            // Generate a non-zero offset to ensure incorrect equations have different results
            incorrectResultOffset = Random.nextInt(-5, 5)
        }
        val result = if (isCorrectEquation) correctResult else correctResult + incorrectResultOffset

        equationTextP1.text = "$number1 $operator $number2 = $result"
        equationTextP2.text = "$number1 $operator $number2 = $result"

        player1Zone.setOnClickListener {
            handleTap(isCorrectEquation, player1Zone, equationTextP1)
            checkGameOver(player1Zone, player2Zone)
        }

        player2Zone.setOnClickListener {
            handleTap(isCorrectEquation, player2Zone, equationTextP2)
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
            delay(GameManager.gameDelayTime)
            isGamePaused = false
        }
    }

    private fun checkGameOver(player1Zone: LinearLayout, player2Zone: LinearLayout) {
        if (++tapCount >= maxGameTaps) {
            player1Zone.isClickable = false
            player2Zone.isClickable = false
            //Delay for players to check results of last round
            GlobalScope.launch(Dispatchers.Main) {
                delay(GameManager.gameDelayTime)
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