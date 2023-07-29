package com.example.reactrumble.minigames

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.reactrumble.R
import com.example.reactrumble.controllers.GameManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("ClickableViewAccessibility")
class FlagsGame : AppCompatActivity() {

	private val gameInstructions = "Tap if flag matches country!"

	private lateinit var flagTimer: CountDownTimer

	private var tapCount: Int = 0
	private var isGamePaused: Boolean = false

	companion object {
		// Number of rounds taken from the Game Manager
		private const val MAX_GAME_TIME = 600000L
		private var maxGameTaps = GameManager.maxRoundsPerMiniGame
		private val COLOR_CORRECT = Color.parseColor("#C947D86B")
		private val COLOR_INCORRECT = Color.parseColor("#D34A4A")
		private val COLOR_DEFAULT = Color.parseColor("#A9A9C8")
	}

	// List of flag images to be used for generation
	private val flags = listOf(
		R.drawable.flag_canada,
		R.drawable.flag_america,
		R.drawable.flag_india,
		R.drawable.flag_china
	)

	// List of country names to be used for generation
	private val countryNames = listOf(
		"Canada",
		"America",
		"India",
		"China"
	)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		maxGameTaps = GameManager.maxRoundsPerMiniGame
		setContentView(R.layout.flags_minigame)
		startGame()
	}

	// This method is run to start the game, it displays the game instructions and then runs the
	// startFlagGeneration() method
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
			startFlagGeneration()
		}
	}

	// This method initializes the player touch zones and uses a helper method to display flags on the screen
	private fun startFlagGeneration() {
		// Start the countdown timer for generating flags
		flagTimer = object : CountDownTimer(MAX_GAME_TIME, GameManager.gameDelayTime) {
			override fun onTick(millisUntilFinished: Long) {
				if (!isGamePaused) {
					val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
					val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
					player1Zone.isEnabled = true
					player1Zone.setBackgroundColor(COLOR_DEFAULT)
					player2Zone.isEnabled = true
					player2Zone.setBackgroundColor(COLOR_DEFAULT)
					displayRandomFlag()
				}
			}

			override fun onFinish() {
				// Call Next Game or Game Over Screen after the game duration is over
				GameManager.nextGame(this@FlagsGame)
			}
		}
		flagTimer.start()
	}

	// This method generates correct and incorrect (by chance) flag and country name pairs and
	// displays them on the screen
	private fun displayRandomFlag() {
		val player1Zone: LinearLayout = findViewById(R.id.player1_zone)
		val player2Zone: LinearLayout = findViewById(R.id.player2_zone)
		val countryNameP1: TextView = findViewById(R.id.countryNameP1)
		val countryNameP2: TextView = findViewById(R.id.countryNameP2)
		val flagP1: ImageView = findViewById(R.id.flagP1)
		val flagP2: ImageView = findViewById(R.id.flagP2)

		// Get a random flag and country name from the lists
		val randomFlagIndex = Random.nextInt(flags.size)
		val randomCountryIndex = Random.nextInt(countryNames.size)

		val flagResId = flags[randomFlagIndex]
		val countryName = countryNames[randomCountryIndex]

		// Set the randomly chosen flag and country name on both player's screens
		flagP1.setImageResource(flagResId)
		flagP1.tag = flagResId
		flagP2.setImageResource(flagResId)
		flagP2.tag = flagResId

		countryNameP1.text = countryName
		countryNameP2.text = countryName

		// Make the flag and country name visible on both player's screens
		flagP1.visibility = View.VISIBLE
		flagP2.visibility = View.VISIBLE
		countryNameP1.visibility = View.VISIBLE
		countryNameP2.visibility = View.VISIBLE

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

	// Method used to check if the flag and country name match based on positions in arrays
	private fun getFlagResource(countryName: String): Int? {
		val index = countryNames.indexOf(countryName)
		return if (index != -1) flags[index] else null
	}

	private fun handleTap(
		currentFlagResId: Int?,
		currentCountryName: String,
		playerZone: LinearLayout,
		countryName: TextView
	) {
		// Handle player tap by checking if the flag matches the country name
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
		// Increment the score and provide feedback to the player
		when (playerZone.id) {
			R.id.player1_zone -> GameManager.playerOneScore++
			R.id.player2_zone -> GameManager.playerTwoScore++
		}
		countryName.text = "AWESOME JOB!"
		updateScoreText()
	}

	private fun decreaseScore(playerZone: LinearLayout, countryName: TextView) {
		// Decrement the score and provide feedback to the player
		when (playerZone.id) {
			R.id.player1_zone -> GameManager.playerOneScore--
			R.id.player2_zone -> GameManager.playerTwoScore--
		}
		countryName.text = "BOO! YOU SUCK!"
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
				flagTimer.cancel()
				flagTimer.onFinish()
			}
		}
	}

	// If the back button is pressed during a game session, it will take you straight
	// to the game over screen
	override fun onBackPressed() {
		GameManager.gameOver(this)
	}
}
