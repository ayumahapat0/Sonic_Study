package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectDeck: AppCompatActivity() {

    private lateinit var deckText: TextView
    private lateinit var selectedDeck: String
    private val availableDecks = mutableListOf<String>() // Store deck names

    private val voiceInputLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                deckText.text = matches[0]
                selectedDeck = matches[0]
            }
        }else{
            Toast.makeText(this, "No result captured", Toast.LENGTH_SHORT).show()
        }
    }

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
        setContentView(R.layout.select_deck_study)

        deckText = findViewById(R.id.selectedDeck)
        val voiceButton = findViewById<Button>(R.id.voiceInputButton)
        val studyButton = findViewById<Button>(R.id.studyButton)

        fetchAvailableDecks()

        voiceButton.setOnClickListener { startVoiceInput() }

        studyButton.setOnClickListener {
            if (availableDecks.contains(selectedDeck)) {
                val intent = Intent(this, Flashcard::class.java)
                intent.putExtra("DECK_NAME", selectedDeck)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Deck not found. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchAvailableDecks() {
        RetrofitClient.apiService.retrieveTables().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    availableDecks.clear()
                    availableDecks.addAll(response.body() ?: emptyList())
                    Toast.makeText(this@SelectDeck, "Retrieved all Decks!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SelectDeck, "No Decks currently stored! Start by adding a Deck!", Toast.LENGTH_SHORT).show()
                    Log.e("API", "Failed to retrieve decks: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(this@SelectDeck, "Error Receiving Decks. Try Again!", Toast.LENGTH_SHORT).show()
                Log.e("API", "Error: ${t.message}")
            }
        })
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the deck name")
        }
        try {
            voiceInputLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Voice input not supported", Toast.LENGTH_SHORT).show()
        }
    }
}
