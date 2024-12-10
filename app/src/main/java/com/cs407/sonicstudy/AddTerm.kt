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
import com.cs407.sonicstudy.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AddTerm : AppCompatActivity() {
    private lateinit var termText: TextView
    private lateinit var defText: TextView
    private lateinit var question: String
    private lateinit var answer: String
    private lateinit var deck: String
    private var termDone: Boolean = false
    private var defDone: Boolean = false
    private fun saveIntoDatabase() {
        if (termDone && defDone) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val database = Database()
                    database.insertData(deck, question, answer)
                    //Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    //Toast.makeText(this, "Failed to save data: ${e.message}", Toast.LENGTH_SHORT)
                    //    .show()
                }
            }
            } else {
                Toast.makeText(this, "Please provide both term and definition!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_term)

        val termButton = findViewById<Button>(R.id.termBtn)
        termText = findViewById(R.id.term)

        val defButton = findViewById<Button>(R.id.defBtn)
        defText = findViewById(R.id.definition)

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

    private fun voiceInput(string: String){
        val question = ""
        val answer = ""
        val language = "en"
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak")
        if (string == "Term"){
            try{
                text.launch(intent)
            }catch (e: Exception){
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }

        else if (string == "Definition"){
            try{
                definition.launch(intent)
            }catch (e: Exception){
                Toast.makeText(this, "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private val text = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if (activityResult.resultCode == RESULT_OK){
            val result = activityResult.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            termText.append(result!![0])
            question = result[0]
            termDone = true
        }
    }

    private val definition = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if (activityResult.resultCode == RESULT_OK){
            val result = activityResult.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            defText.append(result!![0])
            answer = result[0]
            defDone = true
        }
    }




}
