package com.cs407.sonicstudy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DecksHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decks_home)

        val deckRV = findViewById<RecyclerView>(R.id.RVDecks)

        // Here, we have created new array list and added data to it
        val deckModelArrayList: ArrayList<DeckModel> = ArrayList<DeckModel>()
        deckModelArrayList.add(DeckModel("DSA in Java"))
        deckModelArrayList.add(DeckModel("Java Course"))
        deckModelArrayList.add(DeckModel("C++ Course"))
        deckModelArrayList.add(DeckModel("DSA in C++"))
        deckModelArrayList.add(DeckModel("Kotlin for Android"))
        deckModelArrayList.add(DeckModel("Java for Android"))
        deckModelArrayList.add(DeckModel("HTML and CSS"))

        // we are initializing our adapter class and passing our arraylist to it.
        val deckAdapter = DecksAdapter(this, deckModelArrayList)

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        deckRV.layoutManager = linearLayoutManager
        deckRV.adapter = deckAdapter
    }
}