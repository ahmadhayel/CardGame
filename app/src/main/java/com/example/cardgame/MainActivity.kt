package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.os.Handler
import android.os.Looper
import com.example.cardgame.Deck
import com.example.cardgame.PlayingCard
import com.example.cardgame.R

class MainActivity : AppCompatActivity() {

    private lateinit var deck: Deck
    private var currentCard: PlayingCard? = null
    private var nextCard: PlayingCard? = null
    private var score = 0
    private var streak = 0

    private lateinit var currentCardView: ImageView
    private lateinit var nextCardView: ImageView
    private lateinit var scoreView: TextView
    private lateinit var buttonHigher: Button
    private lateinit var buttonLower: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Hämta UI-komponenter
        currentCardView = findViewById(R.id.imageViewCurrentCard)
        nextCardView = findViewById(R.id.imageViewNextCard)
        scoreView = findViewById(R.id.textViewScore)
        buttonHigher = findViewById(R.id.buttonHigher)
        buttonLower = findViewById(R.id.buttonLower)

        // Starta nytt spel
        startNewGame()

        // Lyssnare för gissning
        buttonHigher.setOnClickListener { makeGuess("higher") }
        buttonLower.setOnClickListener { makeGuess("lower") }
    }

    private fun startNewGame() {
        deck = Deck()
        currentCard = deck.drawCard()
        nextCard = deck.drawCard()
        score = 0
        streak = 0

        // Visa bara nuvarande kort, nästa kort dolt tills spelaren gissar
        nextCardView.visibility = View.INVISIBLE
        updateUI()
    }

    private fun makeGuess(guess: String) {
        if (nextCard == null) {
            Toast.makeText(this, "You have finished the deck! You win! 🎉", Toast.LENGTH_LONG).show()
            startNewGame()
            return
        }

        // VISA nästa kort direkt
        nextCardView.setImageResource(getCardImage(nextCard!!))
        nextCardView.visibility = View.VISIBLE

        val correct = when {
            nextCard!!.value == currentCard?.value -> true
            guess == "higher" && nextCard!!.value > currentCard!!.value -> true
            guess == "lower" && nextCard!!.value < currentCard!!.value -> true
            else -> false
        }

        if (correct) {
            score += if (nextCard!!.value == currentCard?.value) 2 else 1
            streak += 1

            // Inaktivera knapparna tills spelaren är redo för nästa drag
            buttonHigher.isEnabled = false
            buttonLower.isEnabled = false

            // Lägg till en "Next" knapp eller vänta på att spelaren klickar igen
            Toast.makeText(this, "Correct! Tap anywhere to continue", Toast.LENGTH_SHORT).show()

            // Aktivera knapparna igen efter kort visats
            Handler(Looper.getMainLooper()).postDelayed({
                currentCard = nextCard
                nextCard = deck.drawCard()
                updateUI()
                nextCardView.visibility = View.INVISIBLE
                buttonHigher.isEnabled = true
                buttonLower.isEnabled = true
            }, 2000)

        } else {
            Toast.makeText(this, "Game Over! Score: $score", Toast.LENGTH_SHORT).show()
            startNewGame()
        }
    }

    private fun getCardImage(card: PlayingCard): Int {
        val valueName = when (card.value) {
            in 2..10 -> card.value.toString()
            11 -> "jack"
            12 -> "queen"
            13 -> "king"
            14 -> "ace"
            else -> "back"
        }

        val suitName = card.suit.lowercase()  // hearts, spades, clubs, diamonds

        // Filformat: card_10_of_clubs
        val resName = "card_${valueName}_of_${suitName}"

        return resources.getIdentifier(resName, "drawable", packageName)
    }

    private fun updateUI() {
        currentCard?.let {
            currentCardView.setImageResource(getCardImage(it))
        }

        // POÄNG
        scoreView.text = "Score: $score"
    }
}