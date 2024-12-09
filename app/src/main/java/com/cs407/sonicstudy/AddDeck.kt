package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.RESULT_OK

class AddDeck : AppCompatActivity()  {
    private lateinit var deckText: TextView
    private lateinit var deck: String
    private var deckDone: Boolean = false
    private fun saveIntoDatabase() {
        if (deckDone) {
            try {
                val database = Database()
                val columns = ArrayList<String>()
                database.createTable(deck, columns, primaryKey = "String")
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please provide both term and definition!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_deck)

        val deckButton = findViewById<Button>(R.id.deckBtn)
        deckText = findViewById(R.id.Deck)

        val saveButton = findViewById<Button>(R.id.saveBtn)

        saveButton.setOnClickListener {
            saveIntoDatabase()
        }

        deckButton.setOnClickListener{ view: View? ->
            voiceInput()
        }
    }

    private fun voiceInput() {
        val language = "en"
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")

        try {
            deckVal.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
        }

    }

    private val deckVal = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if (activityResult.resultCode == RESULT_OK){
            val result = activityResult.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            deckText.append(result!![0])
            deck = result[0]
        }
    }
}