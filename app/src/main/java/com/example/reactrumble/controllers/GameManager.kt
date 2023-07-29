package com.example.reactrumble.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import androidx.appcompat.app.AppCompatActivity
import com.example.reactrumble.custompreferences.GamePreferences
import com.example.reactrumble.minigames.ColorsGame
import com.example.reactrumble.minigames.FlagsGame
import com.example.reactrumble.minigames.MathGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


class GameManager private constructor() {

	companion object {

		//Global variables to keep track of both players scores across all minigames
		var playerOneScore: Int = 0
		var playerTwoScore: Int = 0

		//Initialize default values for game customizations
		var maxRoundsPerMiniGame: Int = 3
		var maxMiniGamesPerMatch: Int = 3
		var gameDelayTime = 2000L

		//Variables to keep track of frequency of minigames played in a match
		var lastGameIndex: Int = -1
		var miniGamesPlayedCount: Int = 0
		private var gameList: Array<Class<out AppCompatActivity>> =
			arrayOf(MathGame::class.java, FlagsGame::class.java, ColorsGame::class.java)

		fun startGame(context: Context, list: Array<Class<out AppCompatActivity>> = gameList) {
			//Get game customization options from shared preferences
			maxRoundsPerMiniGame = GamePreferences.getInstance(context).getNoOFRounds()!!
			maxMiniGamesPerMatch = GamePreferences.getInstance(context).getMaxMiniGamesPerMatch()!!
			gameDelayTime = GamePreferences.getInstance(context).getGameSpeed()!!

			gameList = list
			playerOneScore = 0
			playerTwoScore = 0
			nextGame(context)
		}

		/**
		 * If game is not over, we swtich to a random game.
		 */
		fun nextGame(context: Context) {
			//Delay for players to check results of last round
			GlobalScope.launch(Dispatchers.Main) {
				delay(gameDelayTime)
			}

			if (miniGamesPlayedCount >= maxMiniGamesPerMatch) {
				gameOver(context)
			} else {
				var index: Int
				do {
					index = Random.nextInt(gameList.size)
				} while (index == lastGameIndex && gameList.size > 1) //Ensures we don't get same game twice
				val intent = Intent(context, gameList[index])
				intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
				lastGameIndex = index
				miniGamesPlayedCount++
				context.startActivity(intent)
				(context as Activity?)?.finish()
			}

		}

		/**
		 * If gameOver is called, we move to the gameOver screen.
		 */
		fun gameOver(context: Context) {
			//Calculate final score
			val win = playerOneScore - playerTwoScore
			miniGamesPlayedCount = 0
			val intent = Intent(context, GameOverActivity::class.java)
			intent.putExtra("p1", playerOneScore)
			intent.putExtra("p2", playerTwoScore)
			intent.putExtra("winner", win)
			context.startActivity(intent)
		}

	}
}