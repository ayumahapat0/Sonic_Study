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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyDeleteTerm : AppCompatActivity() {
    private lateinit var termToModify: TextView
    private lateinit var defToModify: TextView
    private lateinit var question: String
    private lateinit var answer: String
    private var termDone: Boolean = false
    private var defDone: Boolean = false

    private fun saveIntoDatabase() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the common menu.xml
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_delete)

        val modifyTermBtn = findViewById<Button>(R.id.modifyTermBtn)
        termToModify = findViewById(R.id.term_modify)

        val modifyDefBtn = findViewById<Button>(R.id.modifyDefBtn)
        defToModify = findViewById(R.id.def_modify)

        val saveChangesBtn = findViewById<Button>(R.id.saveChangesBtn)

        val deleteBtn = findViewById<Button>(R.id.deleteBtn)

        saveChangesBtn.setOnClickListener {
            saveIntoDatabase()
        }

        deleteBtn.setOnClickListener{ view: View? ->

        }

        modifyTermBtn.setOnClickListener{ view: View? ->
            voiceInput("Term")
        }

        modifyDefBtn.setOnClickListener{ view: View? ->
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
            TODO("Not append, but overwrite")
            termToModify.setText(result!![0])
            question = result[0]
            termDone = true
        }
    }

    private val definition = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if (activityResult.resultCode == RESULT_OK){
            val result = activityResult.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            TODO("Not append, but overwrite")
            defToModify.setText(result!![0])
            answer = result[0]
            defDone = true
        }
    }
}