package com.example.reactrumble

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameManager private constructor() {

    companion object {

        var playerOneScore: Int = 0
        var playerTwoScore: Int = 0
        var maxRoundsPerMiniGame: Int = 3 //Get From Preferences, default is 3
        var maxMiniGamesPerMatch: Int = 3 //Get From Preferences, default is 3
        var lastGameIndex: Int = -1
        var miniGamesPlayedCount: Int = 0
        var DELAY_TIME = 2000L
        private lateinit var list: Array<Class<out AppCompatActivity>>

        fun startGame(context: Context, list: Array<Class<out AppCompatActivity>>) {
            maxRoundsPerMiniGame = GamePreferences.getInstance(context).getNoOFRounds() ?: maxRoundsPerMiniGame
            maxMiniGamesPerMatch = GamePreferences.getInstance(context).getMaxMiniGamesPerMatch() ?: maxMiniGamesPerMatch
            this.list = list
            playerOneScore = 0
            playerTwoScore = 0
            nextGame(context)
        }

        /**
         * If game is not over, we swtich to a random game.
         */
        fun nextGame(context: Context) {
            if (miniGamesPlayedCount >= maxMiniGamesPerMatch) {
                gameOver(context)
            } else {
                var index : Int
                do {
                    index = Random.nextInt(list.size)
                } while(index == lastGameIndex && list.size > 1) //Ensures we don't get same game twice
                val intent = Intent(context, list[index])
                lastGameIndex = index
                miniGamesPlayedCount++
                context.startActivity(intent)
            }

        }

        /**
         * If gameOver is called, we move to the gameOver screen.
         */
        fun gameOver(context: Context) {
            val p1Win = playerOneScore > playerTwoScore
            miniGamesPlayedCount = 0
            val intent = Intent(context, GameOverActivity::class.java)
            intent.putExtra("p1", playerOneScore)
            intent.putExtra("p2", playerTwoScore)
            intent.putExtra("winner", p1Win)
            context.startActivity(intent)
        }

    }


}