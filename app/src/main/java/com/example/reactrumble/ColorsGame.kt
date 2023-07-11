package com.example.reactrumble

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ColorsGame : AppCompatActivity() {

    // Initializing placeholder score variable and colorTimer for the minigame
    private var score = 0
    private lateinit var colorTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.colors_minigame)

        startGame()
    }

    // This method is run to start the game, it displays the game instructions and then runs the
    // startCountdown() which in turn runs the startColorsGeneration() method
    private fun startGame() {

        var countdownTextP1 : TextView = findViewById(R.id.countdownTextP1)
        var countdownTextP2 : TextView = findViewById(R.id.countdownTextP2)

        countdownTextP1.text = "Tap correct colors!"
        countdownTextP2.text = "Tap correct colors!"

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000) // Display the game instructions for 3 seconds
            countdownTextP1.text = "3"
            countdownTextP2.text = "3"

            startCountdown()
        }
    }

    // Starts a countdown before the colors start popping up
    private fun startCountdown() {

        var countdownTextP1 : TextView = findViewById(R.id.countdownTextP1)
        var countdownTextP2 : TextView = findViewById(R.id.countdownTextP2)

        colorTimer = object : CountDownTimer(4000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val count = millisUntilFinished / 1000
                countdownTextP1.text = count.toString()
                countdownTextP2.text = count.toString()
            }

            override fun onFinish() {

                countdownTextP1.text = "Go!"
                countdownTextP2.text = "Go!"

                // The "Go!" vanishes after 1 second of appearing
                GlobalScope.launch(Dispatchers.Main) {
                    delay(1000)
                    countdownTextP1.text = ""
                    countdownTextP2.text = ""

                    // Start generating the colors
                    startColorsGeneration()
                }
            }
        }

        colorTimer.start()
    }

    // This method uses a bunch of helper methods to generate correct and incorrect (by chance) colors and displays
    // them on the screen
    private fun startColorsGeneration() {

        colorTimer = object : CountDownTimer(100000, 900) {
            override fun onTick(millisUntilFinished: Long) {

                val colorName = getRandomColorName()
                val colorValue = getRandomColor()
                displayColorText(colorName, colorValue)
            }

            override fun onFinish() {
                // Game over logic goes here
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

    // Placeholder method that checks if color is correct when pressed on it
    private fun setupColorClickListener() {

        var colorTextP1 : TextView = findViewById(R.id.colorTextP1)
        var colorTextP2 : TextView = findViewById(R.id.colorTextP2)

        colorTextP1.setOnClickListener {
            val colorName = colorTextP1.text.toString()
            val textColor = colorTextP1.currentTextColor
            val backgroundColor = colorTextP1.currentTextColor
            if (textColor == backgroundColor) {
                score++
                // Handle correct tap
            } else {
                score--
                // Handle incorrect tap
            }
        }
    }
}
