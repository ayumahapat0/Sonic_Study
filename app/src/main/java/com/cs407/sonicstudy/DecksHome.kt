package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DecksHome : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeckAdapt
    private val decks = mutableListOf(
        "Deck 1" to listOf("What is Java?" to "A programming language.", "What is Android?" to "A mobile OS."),
        "Deck 2" to listOf("What is Kotlin?" to "A programming language.", "What is OOP?" to "Object-Oriented Programming."),
        "Deck 3" to listOf("What is XML?" to "A markup language.", "What is JSON?" to "A data format.")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decks_home)

        recyclerView = findViewById(R.id.decks_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DeckAdapt(decks) { deckName ->
            val intent = Intent(this@DecksHome, FlashcardActivity::class.java)
            intent.putExtra("DECK_NAME", deckName)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        findViewById<ImageButton>(R.id.add_deck_button).setOnClickListener {
            // Add deck button functionality
        }
    }
}
