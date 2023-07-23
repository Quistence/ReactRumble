package com.example.reactrumble

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameManager private constructor() {

    companion object {

        private lateinit var pref: SharedPreferences
        var playerOneScore: Int = 0
        var playerTwoScore: Int = 0
        var maxScore: Int = 5
        var lastGameIndex: Int = -1
        var instance: GameManager? = null
        private lateinit var list: Array<Class<out AppCompatActivity>>

        fun startGame(context: Context, list: Array<Class<out AppCompatActivity>>) {
            if (instance != null) {
                return
            }
            instance = GameManager()
            pref = context.getSharedPreferences("customizationsPreferences",
                AppCompatActivity.MODE_PRIVATE
            )
            this.list = list
            this.playerOneScore = 0
            this.playerTwoScore = 0
            nextGame(context)
        }

        /**
         * If game is not over, we swtich to a random game.
         */
        fun nextGame(context: Context) {
            if (playerOneScore >= maxScore || playerTwoScore >= maxScore) {
                gameOver(context)
            } else {
                var index : Int
                do {
                    index = Random.nextInt(list.size)
                } while(index == lastGameIndex && list.size > 1) //Ensures we don't get same game twice
                val intent = Intent(context, list[index])
                lastGameIndex = index
                context.startActivity(intent)
            }

        }

        /**
         * If gameOver is called, we move to the gameOver screen.
         */
        fun gameOver(context: Context) {
            val p1Win = playerOneScore > playerTwoScore
            val intent = Intent(context, GameOverActivity::class.java)
            intent.putExtra("p1", playerOneScore)
            intent.putExtra("p2", playerTwoScore)
            intent.putExtra("winner", p1Win)
            instance = null
            context.startActivity(intent)
        }

    }


}