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

class AddTerm : AppCompatActivity() {
    private lateinit var termText: TextView
    private lateinit var defText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_term)

        val termButton = findViewById<Button>(R.id.termBtn)
        termText = findViewById(R.id.term)

        val defButton = findViewById<Button>(R.id.defBtn)
        defText = findViewById(R.id.definition)

        termButton.setOnClickListener{ view: View? ->
            voiceInput("Term")
        }

        defButton.setOnClickListener{ view: View? ->
            voiceInput("Definition")
        }
    }

    private fun voiceInput(string: String){
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
        }
    }

    private val definition = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if (activityResult.resultCode == RESULT_OK){
            val result = activityResult.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            defText.append(result!![0])
        }
    }



}
