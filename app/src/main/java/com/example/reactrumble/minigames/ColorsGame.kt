package com.example.reactrumble.minigames

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reactrumble.R
import com.example.reactrumble.controllers.GameManager
import com.example.reactrumble.custompreferences.GamePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("ClickableViewAccessibility")
class ColorsGame : AppCompatActivity() {

	private val gameInstructions = "Tap Correct Colors!"

	private lateinit var gamePreferences: GamePreferences

	private lateinit var colorTimer: CountDownTimer

	private var tapCount: Int = 0
	private var isGamePaused: Boolean = false

	companion object {
		// Number of rounds taken from Game Manager
		private const val MAX_GAME_TIME = 600000L
		private var maxGameTaps = GameManager.maxRoundsPerMiniGame
		private val COLOR_CORRECT = Color.parseColor("#C947D86B")
		private val COLOR_INCORRECT = Color.parseColor("#D34A4A")
		private val COLOR_DEFAULT = Color.parseColor("#A9A9C8")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		gamePreferences = GamePreferences.getInstance(applicationContext)
		maxGameTaps = GameManager.maxRoundsPerMiniGame
		setContentView(R.layout.colors_minigame)
		startGame()
	}

	// This method is run to start the game, it displays the game instructions and then runs the
	// startColorsGeneration() method
	private fun startGame() {

		val countdownTextP1: TextView = findViewById(R.id.countdownTextP1)
		val countdownTextP2: TextView = findViewById(R.id.countdownTextP2)
		val player1instructions: TextView = findViewById(R.id.player1_instructions)
		val player2instructions: TextView = findViewById(R.id.player2_instructions)

		countdownTextP1.text = gameInstructions
		countdownTextP2.text = gameInstructions
		player1instructions.text = gameInstructions
		player2instructions.text = gameInstructions

		// Set Initial Player Scores from previous games
		updateScoreText()

		// Delay the start of the game to allow players to read the instructions
		GlobalScope.launch(Dispatchers.Main) {
			delay(GameManager.gameDelayTime)
			countdownTextP1.text = ""
			countdownTextP2.text = ""
			startColorsGeneration()
		}
	}

	// This method uses a bunch of helper methods to generate correct and incorrect (by chance) colors and displays
	// them on the screen
	private fun startColorsGeneration() {
		// Start the countdown timer for generating colors
		colorTimer = object : CountDownTimer(MAX_GAME_TIME, GameManager.gameDelayTime) {
			override fun onTick(millisUntilFinished: Long) {

				if (!isGamePaused) {
					val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
					val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
					val colorTextP1: TextView = findViewById(R.id.colorTextP1)
					val colorTextP2: TextView = findViewById(R.id.colorTextP2)

					val colorName = getRandomColorName()
					val colorValue = getRandomColor()

					player1Zone.isEnabled = true
					player1Zone.setBackgroundColor(COLOR_DEFAULT)
					player2Zone.isEnabled = true
					player2Zone.setBackgroundColor(COLOR_DEFAULT)

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
				// Call Next Game or Game Over Screen after the game duration is over
				GameManager.nextGame(this@ColorsGame)
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
		val colors: List<Int> = if (gamePreferences.getDarkMode() == true) {
			listOf(Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA)
		} else {
			listOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA)
		}
		return colors[Random.nextInt(colors.size)]
	}

	// Sets the color text, and color of the TextViews for players
	private fun displayColorText(colorName: String, colorValue: Int) {

		var colorTextP1: TextView = findViewById(R.id.colorTextP1)
		var colorTextP2: TextView = findViewById(R.id.colorTextP2)

		colorTextP1.text = colorName
		colorTextP2.text = colorName

		colorTextP1.setTextColor(colorValue)
		colorTextP2.setTextColor(colorValue)
	}

	private fun handleTap(playerZone: LinearLayout, colorText: TextView) {
		// Handle player tap by checking if the tapped color matches the displayed color name
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
		// Increment the score and provide feedback to the player
		when (playerZone.id) {
			R.id.player1_zone -> GameManager.playerOneScore++
			R.id.player2_zone -> GameManager.playerTwoScore++
		}

		// Change color theme based on if dark mode is on
		if (gamePreferences.getDarkMode() == true)
			colorText.setTextColor(Color.WHITE)
		else
			colorText.setTextColor(Color.BLACK)
		colorText.text = "AWESOME JOB!"
		updateScoreText()
	}

	private fun decreaseScore(playerZone: LinearLayout, colorText: TextView) {
		// Decrement the score and provide feedback to the player
		when (playerZone.id) {
			R.id.player1_zone -> GameManager.playerOneScore--
			R.id.player2_zone -> GameManager.playerTwoScore--
		}

		// Change color theme based on if dark mode is on
		if (gamePreferences.getDarkMode() == true)
			colorText.setTextColor(Color.WHITE)
		else
			colorText.setTextColor(Color.BLACK)
		colorText.text = "BOO! YOU SUCK!"
		updateScoreText()
	}

	private fun updateScoreText() {
		// Update the displayed scores for both players
		val player1DisplayScore: TextView = findViewById(R.id.player1_score)
		val player2DisplayScore: TextView = findViewById(R.id.player2_score)
		player1DisplayScore.text = "SCORE: ${GameManager.playerOneScore}"
		player2DisplayScore.text = "SCORE: ${GameManager.playerTwoScore}"
	}

	private fun disablePlayerZones() {
		// Disable player touch zones temporarily to prevent rapid tapping
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
		// Check if the maximum number of rounds has been reached, and if so, end the game
		if (++tapCount >= maxGameTaps) {
			player1Zone.isClickable = false
			player2Zone.isClickable = false

			// Delay before ending the game to allow players to check the results of the last round
			GlobalScope.launch(Dispatchers.Main) {
				delay(GameManager.gameDelayTime)
				colorTimer.cancel()
				colorTimer.onFinish()
			}
		}
	}

	// If the back button is pressed during a game session, it will take you straight
	// to the game over screen
	override fun onBackPressed() {
		GameManager.gameOver(this)
	}
}
