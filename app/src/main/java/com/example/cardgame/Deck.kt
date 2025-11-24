package com.example.cardgame

class Deck {
    private val suits = listOf("hearts", "diamonds", "clubs", "spades")
    private val values = (2..14).toList()
    private val cards = mutableListOf<PlayingCard>()

    init {
        createDeck()
        shuffleDeck()
    }

    private fun createDeck() {
        for (suit in suits) {
            for (value in values) {
                cards.add(PlayingCard(suit, value))
            }
        }
    }

    fun shuffleDeck() {
        cards.shuffle()
    }

    fun drawCard(): PlayingCard? {
        return if (cards.isEmpty()) null else cards.removeAt(0)
    }
}
