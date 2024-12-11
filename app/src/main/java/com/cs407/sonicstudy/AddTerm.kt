package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cs407.sonicstudy.DataModels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddTerm : AppCompatActivity() {
    private lateinit var termToModify: TextView
    private lateinit var defToModify: TextView
    private lateinit var question: String
    private lateinit var answer: String
    private lateinit var deck : String
    private var termDone: Boolean = false
    private var defDone: Boolean = false

    private fun saveIntoDatabase() {
        if (!(question.isEmpty() || answer.isEmpty())){
                val request = DataModels.InsertDataRequest(deck, question, answer)

                RetrofitClient.apiService.insertData(request)
                    .enqueue(object : Callback<DataModels.ApiResponse> {
                        override fun onResponse(
                            call: Call<DataModels.ApiResponse>,
                            response: Response<DataModels.ApiResponse>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("API", "Inserted Data properly")
                                Toast.makeText(this@AddTerm, "Entry has been added!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this@AddTerm, "Entry wasn't added! Try Again!", Toast.LENGTH_SHORT).show()
                                Log.e("API", "ERROR: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(call: Call<DataModels.ApiResponse>, t: Throwable) {
                            Toast.makeText(this@AddTerm, "Failed to Add Entry! Try Again!", Toast.LENGTH_SHORT).show()
                            Log.d("API", "Failure: ${t.message}")
                        }

                    })
        } else{
            Toast.makeText(this, "question and answer not filled in yet!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the common menu.xml
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
        setContentView(R.layout.add_term)

        val termButton = findViewById<Button>(R.id.termBtn)
        termToModify = findViewById(R.id.term)

        deck = intent.getStringExtra("title").toString()

        val defButton = findViewById<Button>(R.id.defBtn)
        defToModify = findViewById(R.id.definition)
        val saveButton = findViewById<Button>(R.id.saveBtn)

        saveButton.setOnClickListener {
            saveIntoDatabase()
        }

        termButton.setOnClickListener{ view: View? ->
            voiceInput("Term")
        }

        defButton.setOnClickListener{ view: View? ->
            voiceInput("Definition")
        }
    }

    private fun voiceInput(string: String) {
        val language = "en"
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")
        }
        try {
            when (string) {
                "Term" -> textLauncher.launch(intent)
                "Definition" -> definitionLauncher.launch(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Something went wrong launching this activity! Try Again!", Toast.LENGTH_SHORT).show()
            Log.e("ModifyDeleteTerm", "Error in voiceInput: ${e.message}", e)
        }
    }

    private val textLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val result = activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                termToModify.text = result[0]
                question = result[0]
                termDone = true
            } else {
                Toast.makeText(this, "No result captured", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val definitionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            val result = activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                defToModify.text = result[0]
                answer = result[0]
                defDone = true
            } else {
                Toast.makeText(this, "No result captured", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
