package com.example.reactrumble

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("ClickableViewAccessibility")
class ColorsGame : AppCompatActivity() {

    private val gameInstructions = "Tap Correct Colors!"

    private val PREFS_FILENAME = "customizationsPreferences"
    private lateinit var preferences: SharedPreferences

    private var player1score = 0
    private var player2score = 0
    private lateinit var colorTimer: CountDownTimer

    private var tapCount: Int = 0
    private var isGamePaused: Boolean = false
    private var isTimerRunning: Boolean = false

    companion object {
        //Can be configured from GameEngine
        private const val MAX_GAME_TAPS = 5
        private const val MAX_GAME_TIME = 60000L
        private const val DELAY_TIME = 900L
        private val COLOR_CORRECT = Color.parseColor("#C947D86B")
        private val COLOR_INCORRECT = Color.parseColor("#D34A4A")
        private val COLOR_DEFAULT = Color.parseColor("#A9A9C8")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = getSharedPreferences(PREFS_FILENAME, MODE_PRIVATE)

        if(preferences.getBoolean("is_dark_mode", false))
            setContentView(R.layout.dark_colors_minigame)
        else
            setContentView(R.layout.colors_minigame)

        startGame()
    }

    // This method is run to start the game, it displays the game instructions and then runs the
    // startCountdown() which in turn runs the startColorsGeneration() method
    private fun startGame() {

        val countdownTextP1: TextView = findViewById(R.id.countdownTextP1)
        val countdownTextP2: TextView = findViewById(R.id.countdownTextP2)
        val player1instructions: TextView = findViewById(R.id.player1_instructions)
        val player2instructions: TextView = findViewById(R.id.player2_instructions)

        countdownTextP1.text = gameInstructions
        countdownTextP2.text = gameInstructions
        player1instructions.text = gameInstructions
        player2instructions.text = gameInstructions

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            countdownTextP1.text = "3"
            countdownTextP2.text = "3"
            startCountdown()
        }
    }

    // Starts a countdown before the colors start popping up
    private fun startCountdown() {
        val countdownTextP1: TextView = findViewById(R.id.countdownTextP1)
        val countdownTextP2: TextView = findViewById(R.id.countdownTextP2)

        colorTimer = object : CountDownTimer(4000, 1000) {
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
                startColorsGeneration()
            }
        }

        colorTimer.start()
    }

    // This method uses a bunch of helper methods to generate correct and incorrect (by chance) colors and displays
    // them on the screen
    private fun startColorsGeneration() {
        colorTimer = object : CountDownTimer(MAX_GAME_TIME, DELAY_TIME) {
            override fun onTick(millisUntilFinished: Long) {

                if (!isGamePaused) {
                    val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
                    val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
                    val colorTextP1: TextView = findViewById(R.id.colorTextP1)
                    val colorTextP2: TextView = findViewById(R.id.colorTextP2)

                    val colorName = getRandomColorName()
                    val colorValue = getRandomColor()
                    displayColorText(colorName, colorValue)

                    player1Zone.setOnClickListener {
                        handleTap(player1Zone, colorTextP1)
                        checkGameOver(player1Zone, player2Zone)
                    }

                    player2Zone.setOnClickListener {
                        handleTap(player2Zone, colorTextP2)
                        checkGameOver(player1Zone, player2Zone)
                    }
                }
            }

            override fun onFinish() {
                val colorTextP1: TextView = findViewById(R.id.colorTextP1)
                val colorTextP2: TextView = findViewById(R.id.colorTextP2)
                colorTextP1.setTextColor(Color.BLACK)
                colorTextP2.setTextColor(Color.BLACK)
                colorTextP1.text = "Game Over! Score: $player1score"
                colorTextP2.text = "Game Over! Score: $player2score"
                //Call Next Game or Game Over Screen
            }
        }

        colorTimer.start()
    }

    // Returns a random color name from a local array
    private fun getRandomColorName(): String {
        val colorNames = listOf("Red", "Green", "Blue", "Pink")
        return colorNames[Random.nextInt(colorNames.size)]
    }

    // Returns a random color value from a local array
    private fun getRandomColor(): Int {
        val colors = listOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA)
        return colors[Random.nextInt(colors.size)]
    }

    // Sets the color text, and color of the TextViews for players
    private fun displayColorText(colorName: String, colorValue: Int) {

        var colorTextP1 : TextView = findViewById(R.id.colorTextP1)
        var colorTextP2 : TextView = findViewById(R.id.colorTextP2)

        colorTextP1.text = colorName
        colorTextP2.text = colorName

        colorTextP1.setTextColor(colorValue)
        colorTextP2.setTextColor(colorValue)
    }

    private fun handleTap(playerZone: LinearLayout, colorText: TextView) {

        val colorName = colorText.text.toString()
        val textColor = colorText.currentTextColor

        val correctColorValue = when (colorName) {
            "Red" -> Color.RED
            "Green" -> Color.GREEN
            "Blue" -> Color.BLUE
            "Pink" -> Color.MAGENTA
            else -> COLOR_DEFAULT // Set a default color for unknown color names
        }

        if (textColor == correctColorValue) {
            playerZone.setBackgroundColor(COLOR_CORRECT)
            increaseScore(playerZone, colorText)
        } else {
            playerZone.setBackgroundColor(COLOR_INCORRECT)
            decreaseScore(playerZone, colorText)
        }

        disablePlayerZones()
    }

    private fun increaseScore(playerZone: LinearLayout, colorText: TextView) {
        when (playerZone.id) {
            R.id.player1_zone -> player1score++
            R.id.player2_zone -> player2score++
        }
        colorText.setTextColor(Color.BLACK)
        colorText.text = "AWESOME JOB!"
        updateScoreText()
    }

    private fun decreaseScore(playerZone: LinearLayout, colorText: TextView) {
        when (playerZone.id) {
            R.id.player1_zone -> player1score--
            R.id.player2_zone -> player2score--
        }
        colorText.setTextColor(Color.BLACK)
        colorText.text = "BOO! YOU SUCK!"
        updateScoreText()
    }

    private fun updateScoreText() {
        val player1DisplayScore: TextView = findViewById(R.id.player1_score)
        val player2DisplayScore: TextView = findViewById(R.id.player2_score)
        player1DisplayScore.text = "SCORE: $player1score"
        player2DisplayScore.text = "SCORE: $player2score"
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
        if (++tapCount >= MAX_GAME_TAPS) {
            colorTimer.cancel()
            colorTimer.onFinish()
            player1Zone.isClickable = false
            player2Zone.isClickable = false
        }
    }

}
