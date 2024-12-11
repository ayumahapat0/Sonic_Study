package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Flashcard : AppCompatActivity() {

    private lateinit var flashcard: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    private var isQuestion = true
    private var currIndex = 0
    private val questionsAndAnswers = mutableListOf<Pair<String, String>>() // Store Q&A pairs

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when (item.itemId){
            R.id.decks -> {
                val intent = Intent(this, DecksHome::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
                return true
            }
            R.id.study -> {
                val intent = Intent(this, SelectDeck::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flashcard_layout)

        flashcard = findViewById(R.id.flashcards)
        showAnswerButton = findViewById(R.id.show_answer_button)
        prevButton = findViewById(R.id.prev_button)
        nextButton = findViewById(R.id.next_button)

        // Get the selected deck name from the Intent
        val deckName = intent.getStringExtra("DECK_NAME") ?: "Unknown Deck"
        fetchDeckData(deckName)

        // Toggle between Question and Answer
        showAnswerButton.setOnClickListener {
            isQuestion = !isQuestion
            updateFlashcard()
        }

        nextButton.setOnClickListener {
            if (currIndex < questionsAndAnswers.size - 1) {
                currIndex++
                isQuestion = true
                updateFlashcard()
            } else {
                Toast.makeText(this, "No more cards!", Toast.LENGTH_SHORT).show()
            }
        }

        prevButton.setOnClickListener {
            if (currIndex > 0) {
                currIndex--
                isQuestion = true
                updateFlashcard()
            } else {
                Toast.makeText(this, "No previous cards!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchDeckData(deckName: String) {
        RetrofitClient.apiService.retrieveData(DataModels.RetrieveDataRequest(deckName)).enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        questionsAndAnswers.clear()
                        for (item in data) {
                            val question = item["question"].toString()
                            val answer = item["answer"].toString()
                            questionsAndAnswers.add(question to answer)
                        }
                        currIndex = 0
                        isQuestion = true
                        updateFlashcard()
                    } else {
                        Toast.makeText(this@Flashcard, "No study cards found!", Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(this@Flashcard, "Failed to load study cards! Try Again!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Toast.makeText(this@Flashcard, "Failed to load study cards! ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Update the flashcard view with the current Question/Answer
    private fun updateFlashcard() {
        if (questionsAndAnswers.isNotEmpty()) {
            val (question, answer) = questionsAndAnswers[currIndex]
            flashcard.text = if (isQuestion) question else answer
            showAnswerButton.text = if (isQuestion) "Show Answer" else "Show Question"
        } else {
            flashcard.text = "No cards available"
        }
    }
}
