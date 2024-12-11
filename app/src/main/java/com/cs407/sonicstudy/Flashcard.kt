package com.cs407.sonicstudy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FlashcardActivity : AppCompatActivity() {

    private lateinit var flashcardView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private var isQuestion = true
    private var currentIndex = 0
    private val questionsAndAnswers = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_layout)

        flashcardView = findViewById(R.id.flashcards)
        showAnswerButton = findViewById(R.id.show_answer_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        val deckName = intent.getStringExtra("DECK_NAME") ?: "Unknown Deck"
        loadDeckData(deckName)

        showAnswerButton.setOnClickListener {
            isQuestion = !isQuestion
            updateFlashcard()
        }

        nextButton.setOnClickListener {
            if (currentIndex < questionsAndAnswers.size - 1) {
                currentIndex++
                isQuestion = true
                updateFlashcard()
            }
        }

        prevButton.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                isQuestion = true
                updateFlashcard()
            }
        }
    }

    private fun loadDeckData(deckName: String) {
        when (deckName) {
            "Deck 1" -> questionsAndAnswers.addAll(
                listOf("What is Java?" to "A programming language.", "What is Android?" to "A mobile OS.")
            )
            "Deck 2" -> questionsAndAnswers.addAll(
                listOf("What is Kotlin?" to "A programming language.", "What is OOP?" to "Object-Oriented Programming.")
            )
            "Deck 3" -> questionsAndAnswers.addAll(
                listOf("What is XML?" to "A markup language.", "What is JSON?" to "A data format.")
            )
        }
        currentIndex = 0
        isQuestion = true
        updateFlashcard()
    }

    private fun updateFlashcard() {
        val (question, answer) = questionsAndAnswers[currentIndex]
        flashcardView.text = if (isQuestion) question else answer
        showAnswerButton.text = if (isQuestion) "Show Answer" else "Show Question"
    }
}
