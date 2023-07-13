package com.example.reactrumble

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import kotlin.random.Random

class GameManager private constructor(var list : Array<Class <out AppCompatActivity>>, var maxScore : Int) {

    companion object {

        var instance : GameManager? = null

        var playerOneScore : Int
        var playerTwoScore : Int
        var maxScore: Int = 0
        var gameStarted = false
        private lateinit var list : Array<Class <out AppCompatActivity>>

        init {
            playerOneScore = 0
            playerTwoScore = 0
        }

        fun initialize(list: Array<Class<out AppCompatActivity>>, maxScore: Int) {
            if(instance != null) {
                return
            }
            instance = GameManager(list, maxScore)
            this.maxScore = maxScore
            this.list = list
        }

        fun startGame(context: Context) {
            if(!gameStarted) {
                nextGame(context)
                gameStarted = true
            }
        }

        /**
         * If game is not over, we swtich to a random game.
         */
        fun nextGame(context: Context) {
            if (playerOneScore >= maxScore || playerTwoScore >= maxScore) {
                gameOver(context)
            } else {
                val index = Random.nextInt(list.size)
                val intent = Intent(context, list[index])
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
            context.startActivity(intent)
            instance = null
        }

    }


}