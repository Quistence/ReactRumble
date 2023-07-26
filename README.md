# CSCI 4176/5708 - Summer 2023 - Mobile Computing Group 3 Project


* *Date Created*: 27 July 2023


## Group 3 Members

* Akshay Patel – B00925429
* Elyas Al Belushi – B00858785
* Logan Brufatto - B00881950
* Rishi Vasa – B00902815
* Ross Brousseau - B00332567


## Group 3 GitLab URL
* https://git.cs.dal.ca/vasa/s23-mobile-computing-project-group-3/-/tree/main
 * Code has also been uploaded on Brightspace as a zip file

## Getting Started

### Requirements
* Android Studio
* Java SDK 17
* An Android Emulator Device setup (Preferably Pixel 3a, with API 33)
* Minimum Android SDK Version 25
* Target Android SDK Version 33

### Running the app

1) Extract the code from the Zip file

2) Open Android Studio and import project

3) Build the app

4) Start the Android Virtual Device/Connect your phone to the PC

5) Run the app


## Sources Used

### 1) General help with Kotlin
- “Stack Overflow - Where Developers Learn, Share, & Build Careers,” Stack Overflow, 2019. http://stackoverflow.com
<br />
- “Kotlin docs | Kotlin,” Kotlin Help. https://kotlinlang.org/docs/home.html
 <br />
- “Android Developers,” Android Developers, 2018. https://developer.android.com/
<br /> <br />
* Changing the app name and logo: https://stackoverflow.com/questions/13272694/android-change-the-app-name-and-logo
<br />
* Handling Back button Press: https://stackoverflow.com/questions/6204972/override-dialog-onbackpressed
<br />
* Adding Null Safety: https://kotlinlang.org/docs/null-safety.html
<br />
* For Divinding Layouts based on Guidelines: https://developer.android.com/reference/androidx/constraintlayout/widget/Guideline
<br />
* For Customizing Switch: https://www.youtube.com/watch?v=5xMPLe1gnOA
### 2) For generating some Layout XMLs quickly and spacing them out well:
- OpenAI, “ChatGPT,” chat.openai.com, 2023. https://chat.openai.com/  
* math_minigame.xml 's LinearLayout was generated but ChatGPT but modified for our use. The other two minigames use the same layout as well

### 3) For working with Countdown Timers in Kotlin
* https://medium.com/@olajhidey/working-with-countdown-timer-in-android-studio-using-kotlin-39fd7826e205
* Example:
```
equationTimer = object : CountDownTimer(MAX_GAME_TIME, GameManager.gameDelayTime) {
    override fun onTick(millisUntilFinished: Long) {
        if (!isGamePaused) {
            /*     Other Code      */
            generateEquation()
        }
    }
    override fun onFinish() {
        //Call Next Game or Game Over Screen
        GameManager.nextGame(this@MathGame)
    }
}
```

### 4) For Using Images of country flags (Open Source)
- “Flagpack,” flagpack.xyz. https://flagpack.xyz/

## Acknowledgments

* Google
* StackOverflow
* ChatGPT
* Kotlin Documentation
* Professor and TA for the course


