package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DecksHome : AppCompatActivity() {

    private lateinit var deckAdapter: DecksAdapter
    private val tables: MutableList<String> = mutableListOf()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.decks -> {
                val intent = Intent(this, DecksHome::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
                return true
            }
            R.id.study -> {
                if (tables.isEmpty()) {
                    Toast.makeText(this, "No study decks available to study!", Toast.LENGTH_SHORT).show()
                } else {
                    showDeckSelectionDialog() // Show deck selection dialog
                }
                return true
            }
            R.id.action_home -> {
                Toast.makeText(this, "Decks Selected:", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_decks_home)

            val addButton = findViewById<ImageButton>(R.id.decks_home_addBtn)
            val delButton = findViewById<ImageButton>(R.id.decks_home_delBtn)

            addButton.setOnClickListener(){
                val intent = Intent(this, AddDeck::class.java)
                startActivity(intent)
            }

            delButton.setOnClickListener() {
                val intent = Intent(this, DeleteDeck::class.java)
                startActivity(intent)
            }

        val deckRV = findViewById<RecyclerView>(R.id.RVDecks)
        deckAdapter = DecksAdapter(this, ArrayList()) { clickedDeck ->
            Toast.makeText(this, "Clicked on: ${clickedDeck.get_deck_name()}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SelectedDeck::class.java)
            intent.putExtra("SelectedDeck", clickedDeck.get_deck_name())
            startActivity(intent)
        }


            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            deckRV.layoutManager = linearLayoutManager
            deckRV.adapter = deckAdapter

        fetchDecks()
    }

    private fun fetchDecks() {
        RetrofitClient.apiService.retrieveTables().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        tables.clear()
                        tables.addAll(data)
                        val deckModelArrayList = ArrayList<DeckModel>()
                        for (item in tables) {
                            deckModelArrayList.add(DeckModel(item))
                        }
                        deckAdapter.updateData(deckModelArrayList) // Update the RecyclerView adapter
                    } else {
                        Log.d("API", "No Tables Received")
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }

    private fun showDeckSelectionDialog() {
        val deckArray = tables.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Select a Deck to Study")
            .setItems(deckArray) { _, which ->
                val selectedDeck = deckArray[which]
                val intent = Intent(this, Flashcard::class.java)
                intent.putExtra("DECK_NAME", selectedDeck)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
