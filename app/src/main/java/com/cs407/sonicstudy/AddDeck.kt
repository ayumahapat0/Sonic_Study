package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddDeck : AppCompatActivity()  {
    private lateinit var deckText: TextView
    private lateinit var deck: String
    private var deckDone: Boolean = false

    private fun saveIntoDatabase() {
        if (deckDone) {
            val list: ArrayList<String> = ArrayList<String>()
            list.add("id INT")
            list.add("question TEXT NOT NULL")
            list.add("answer TEXT NOT NULL")
            list.add("id")


            val request = DataModels.CreateTableRequest(deck, list, "id")

            RetrofitClient.apiService.createTable(request)
                .enqueue(object : Callback<DataModels.ApiResponse> {
                    override fun onResponse(
                        call: Call<DataModels.ApiResponse>,
                        response: Response<DataModels.ApiResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddDeck, "New Deck has been created!", Toast.LENGTH_SHORT).show()
                            Log.d("API", "Created Table Properly")
                        }else{
                            Toast.makeText(this@AddDeck, "Something went wrong with adding this deck. Try Again!", Toast.LENGTH_SHORT).show()
                            Log.e("API", "ERROR: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<DataModels.ApiResponse>, t: Throwable) {
                        Toast.makeText(this@AddDeck, "Something went wrong with adding this deck. Try Again!", Toast.LENGTH_SHORT).show()
                        Log.d("API", "Failure: ${t.message}")
                    }

                })
        } else {
            Toast.makeText(this, "Please provide both term and definition!", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Something went wrong launching this activity! Try Again!", Toast.LENGTH_SHORT).show()
        }

    }

    private val deckVal = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if (activityResult.resultCode == RESULT_OK){
            val result = activityResult.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            deckText.text = result!![0]
            deck = result[0]
            deckDone = true
        } else {
            Toast.makeText(this, "No result captured", Toast.LENGTH_SHORT).show()
        }
    }
}