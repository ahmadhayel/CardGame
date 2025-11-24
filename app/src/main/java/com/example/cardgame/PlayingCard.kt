package com.example.cardgame

data class PlayingCard(
    val suit: String,   // ex: "Hearts", "Diamonds", "Clubs", "Spades"
    val value: Int      // 2-14 (J=11, Q=12, K=13, A=14)
)
