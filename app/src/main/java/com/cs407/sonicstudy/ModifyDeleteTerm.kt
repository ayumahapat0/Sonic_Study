package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ModifyDeleteTerm : AppCompatActivity() {
    private lateinit var termToModify: TextView
    private lateinit var defToModify: TextView
    private lateinit var question: String
    private lateinit var answer: String
    private lateinit var deck : String
    private lateinit var id : String
    private var termDone: Boolean = false
    private var defDone: Boolean = false

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
        setContentView(R.layout.activity_modify_delete)

        deck = intent.getStringExtra("title").toString()
        id = intent.getStringExtra("id").toString()

        Log.d("API", deck)
        Log.d("API", id)

        val modifyTermBtn = findViewById<Button>(R.id.modifyTermBtn)
        termToModify = findViewById(R.id.term_modify)

        val modifyDefBtn = findViewById<Button>(R.id.modifyDefBtn)
        defToModify = findViewById(R.id.def_modify)

        val saveChangesBtn = findViewById<Button>(R.id.saveChangesBtn)

        val deleteBtn = findViewById<Button>(R.id.deleteBtn)

        saveChangesBtn.setOnClickListener {
            saveIntoDatabase()
        }

        deleteBtn.setOnClickListener { view: View? ->
            delete()
        }

        modifyTermBtn.setOnClickListener {
            Log.d("ModifyDeleteTerm", "Modify Term button clicked")
            voiceInput("Term")
        }

        modifyDefBtn.setOnClickListener {
            Log.d("ModifyDeleteTerm", "Modify Definition button clicked")
            voiceInput("Definition")
        }
    }

    private fun delete(){
        val request = DataModels.DeleteDataRequest(deck, "id = $id")

        RetrofitClient.apiService.deleteData(request).enqueue(object : Callback<DataModels.ApiResponse> {
            override fun onResponse(
                call: Call<DataModels.ApiResponse>,
                response: Response<DataModels.ApiResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@ModifyDeleteTerm, "Entry has been deleted!", Toast.LENGTH_SHORT).show()
                    Log.d("API", "deleted term!")
                }else{
                    Toast.makeText(this@ModifyDeleteTerm, "Something went wrong trying to delete this entry! Try Again!", Toast.LENGTH_SHORT).show()
                    Log.d("API", "Error: {${response.errorBody()?.toString()}}")
                }
            }

            override fun onFailure(call: Call<DataModels.ApiResponse>, t: Throwable) {
                Toast.makeText(this@ModifyDeleteTerm, "Something went wrong trying to delete this entry! Try Again!", Toast.LENGTH_SHORT).show()
                Log.d("API", "Failure: ${t.message}")
            }

        })


    }

    private fun saveIntoDatabase() {
        val request = DataModels.UpdateDataRequest(deck, question, answer, "id = $id")

        RetrofitClient.apiService.updateData(request).enqueue(object : Callback<DataModels.ApiResponse> {
            override fun onResponse(
                call: Call<DataModels.ApiResponse>,
                response: Response<DataModels.ApiResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(this@ModifyDeleteTerm, "Entry has been updated!", Toast.LENGTH_SHORT).show()
                    Log.d("API", "entry has been updated")
                }else{
                    Toast.makeText(this@ModifyDeleteTerm, "Something went wrong updating this entry! Try Again!", Toast.LENGTH_SHORT).show()
                    Log.d("API", "Error: {${response.errorBody()?.toString()}" )
                }
            }

            override fun onFailure(call: Call<DataModels.ApiResponse>, t: Throwable) {
                Toast.makeText(this@ModifyDeleteTerm, "Something went wrong updating this entry! Try Again!", Toast.LENGTH_SHORT).show()
                Log.d("API", "Failure: ${t.message}")
            }

        })
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
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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