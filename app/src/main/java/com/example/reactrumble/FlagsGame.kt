package com.example.reactrumble

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("ClickableViewAccessibility")
class FlagsGame : AppCompatActivity() {

    private val gameInstructions = "Tap if flag matches country!"

    private var player1score = 0
    private var player2score = 0
    private lateinit var flagTimer: CountDownTimer

    private var tapCount: Int = 0
    private var isGamePaused: Boolean = false
    private var isTimerRunning: Boolean = false

    companion object {
        //Can be configured from GameEngine
        private const val MAX_GAME_TAPS = 5
        private const val MAX_GAME_TIME = 60000L
        private const val DELAY_TIME = 1000L
        private val COLOR_CORRECT = Color.parseColor("#C947D86B")
        private val COLOR_INCORRECT = Color.parseColor("#D34A4A")
        private val COLOR_DEFAULT = Color.parseColor("#A9A9C8")
    }

    private val flags = listOf(
        R.drawable.flag_canada,
        R.drawable.flag_america,
        R.drawable.flag_india,
        R.drawable.flag_china
    )

    private val countryNames = listOf(
        "Canada",
        "America",
        "India",
        "China"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flags_minigame)
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

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)
            countdownTextP1.text = "3"
            countdownTextP2.text = "3"
            startCountdown()
        }
    }

    private fun startCountdown() {
        val countdownTextP1: TextView = findViewById(R.id.countdownTextP1)
        val countdownTextP2: TextView = findViewById(R.id.countdownTextP2)

        flagTimer = object : CountDownTimer(4000, 1000) {
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
                startFlagGeneration()
            }
        }

        flagTimer.start()
    }

    private fun startFlagGeneration() {
        flagTimer = object : CountDownTimer(MAX_GAME_TIME, DELAY_TIME) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isGamePaused) {
                    displayRandomFlag()
                }
            }

            override fun onFinish() {
                val countryNameP1: TextView = findViewById(R.id.countryNameP1)
                val countryNameP2: TextView = findViewById(R.id.countryNameP2)
                countryNameP1.text = "Game Over! Score: $player1score"
                countryNameP2.text = "Game Over! Score: $player2score"
                //Call Next Game or Game Over Screen
            }
        }
        flagTimer.start()
    }

    private fun displayRandomFlag() {
        val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
        val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
        val countryNameP1: TextView = findViewById(R.id.countryNameP1)
        val countryNameP2: TextView = findViewById(R.id.countryNameP2)
        val flagP1: ImageView = findViewById(R.id.flagP1)
        val flagP2: ImageView = findViewById(R.id.flagP2)

        val randomFlagIndex = Random.nextInt(flags.size)
        val randomCountryIndex = Random.nextInt(countryNames.size)

        val flagResId = flags[randomFlagIndex]
        val countryName = countryNames[randomCountryIndex]

        flagP1.setImageResource(flagResId)
        flagP1.tag = flagResId
        flagP2.setImageResource(flagResId)
        flagP2.tag = flagResId

        countryNameP1.text = countryName
        countryNameP2.text = countryName

        flagP1.visibility = View.VISIBLE
        flagP2.visibility = View.VISIBLE
        countryNameP1.visibility = View.VISIBLE
        countryNameP2.visibility= View.VISIBLE

        player1Zone.setOnClickListener {
            val currentCountryName = countryNameP1.text.toString()
            val currentFlagResId = flagP1.tag as? Int


            handleTap(currentFlagResId, currentCountryName, player1Zone, countryNameP1)
            checkGameOver(player1Zone, player2Zone)
        }

        player2Zone.setOnClickListener {
            val currentCountryName = countryNameP2.text.toString()
            val currentFlagResId = flagP2.tag as? Int

            handleTap(currentFlagResId, currentCountryName, player2Zone, countryNameP2)
            checkGameOver(player1Zone, player2Zone)
        }

    }

    private fun getFlagResource(countryName: String): Int? {
        val index = countryNames.indexOf(countryName)
        return if (index != -1) flags[index] else null
    }

    private fun handleTap(currentFlagResId: Int?, currentCountryName: String, playerZone: LinearLayout, countryName: TextView) {

        val correctFlagResId = getFlagResource(currentCountryName)

        if (currentFlagResId == correctFlagResId) {
            playerZone.setBackgroundColor(COLOR_CORRECT)
            increaseScore(playerZone, countryName)
        } else {
            playerZone.setBackgroundColor(COLOR_INCORRECT)
            decreaseScore(playerZone, countryName)
        }
        disablePlayerZones()
    }

    private fun increaseScore(playerZone: LinearLayout, countryName: TextView) {
        when (playerZone.id) {
            R.id.player1_zone -> player1score++
            R.id.player2_zone -> player2score++
        }
        countryName.text = "AWESOME JOB!"
        updateScoreText()
    }

    private fun decreaseScore(playerZone: LinearLayout, countryName: TextView) {
        when (playerZone.id) {
            R.id.player1_zone -> player1score--
            R.id.player2_zone -> player2score--
        }
        countryName.text = "BOO! YOU SUCK!"
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
            flagTimer.cancel()
            flagTimer.onFinish()
            player1Zone.isClickable = false
            player2Zone.isClickable = false
        }
    }
}