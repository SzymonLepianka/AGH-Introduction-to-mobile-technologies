package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var score = 0;
    private var gamestarted = false
    private lateinit var timer: CountDownTimer
    internal lateinit var button: Button
    internal lateinit var scoreLabel: TextView
    internal lateinit var timeLabel: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        scoreLabel = findViewById(R.id.scoreLabel)
        timeLabel = findViewById(R.id.timeLeftLabel)
        initValues()
        button.setOnClickListener { view ->
            buttonClicked()
        }
        timer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                setTimeLabel(millisUntilFinished / 1000)
            }

            override fun onFinish() {
                gameOver()
            }
        }

    }

    private fun gameOver() {
        val text = getString(R.string.gameOver, score)
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
        score = 0
        button.text = "Start"
        gamestarted = false
    }

    private fun setTimeLabel(l: Long) {
        val timeText = getString(R.string.timeLabel, l)
        timeLabel.text = timeText
    }

    private fun initValues() {
        val text = getString(R.string.scoreLabel, 0)
        scoreLabel.text = text

        setTimeLabel(0)

    }

    private fun buttonClicked() {
        if (!gamestarted) {
            gamestarted = true
            button.text = "Tap me"
            timer.start()
        } else {
            score += 1
            val text = getString(R.string.scoreLabel, score)
            scoreLabel.text = text
        }
        println("Button Clicked!")
    }
}