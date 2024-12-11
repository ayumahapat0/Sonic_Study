package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
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
                            Log.d("API", "Created Table Properly")
                        }else{
                            Log.e("API", "ERROR: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<DataModels.ApiResponse>, t: Throwable) {
                        Log.d("API", "Failure: ${t.message}")
                    }

                })
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
            deckDone = true
        }
    }
}