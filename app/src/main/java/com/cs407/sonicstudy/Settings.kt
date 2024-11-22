package com.cs407.sonicstudy


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings: AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var deckSpinner: Spinner
    private lateinit var studySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

//        val message = intent.getStringExtra("EXTRA_MESSAGE")

//        val resultTextView = findViewById<TextView>(R.id.resultTextView)
//        resultTextView.text = message

         deckSpinner = findViewById(R.id.numDecks)
         ArrayAdapter.createFromResource(
             this,
             R.array.num_decks,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             deckSpinner.adapter = adapter
         }

         studySpinner = findViewById(R.id.studyLimit)
         ArrayAdapter.createFromResource(
             this,
             R.array.study_limit,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             studySpinner.adapter = adapter
         }

        deckSpinner.onItemSelectedListener = this
        studySpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        parent?.getItemAtPosition(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do Nothing
    }
}