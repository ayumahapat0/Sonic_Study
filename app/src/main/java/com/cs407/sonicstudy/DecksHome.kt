package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DecksHome : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when (item.itemId){
            R.id.decks -> {
                Toast.makeText(this, "Decks Selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DecksHome::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
//                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
                return true
            }
            R.id.study -> {
                Toast.makeText(this, "Study selected", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_home -> {
                Toast.makeText(this, "Decks Selected", Toast.LENGTH_SHORT).show()
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

            val deckRV = findViewById<RecyclerView>(R.id.RVDecks)
            val tables: ArrayList<String> = ArrayList<String>()

            // Initialize the adapter here so we can update it later
            val deckAdapter = DecksAdapter(this, ArrayList()) { clickedDeck ->
                Toast.makeText(this, "Clicked on: ${clickedDeck.get_deck_name()}", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, SelectedDeck::class.java)
                intent.putExtra("SelectedDeck", clickedDeck.get_deck_name())
                startActivity(intent)
            }

            // Set up RecyclerView with LayoutManager and empty adapter first
            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            deckRV.layoutManager = linearLayoutManager
            deckRV.adapter = deckAdapter

            // API call to retrieve table data
            RetrofitClient.apiService.retrieveTables().enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data != null) {
                            tables.clear() // Clear any old data
                            tables.addAll(data) // Add new data from API
                            Log.d("API", "Tables Received: $tables")

                            // After receiving data, update the adapter
                            val deckModelArrayList = ArrayList<DeckModel>()
                            for (item in tables) {
                                deckModelArrayList.add(DeckModel(item))
                            }

                            // Update adapter with the new data
                            deckAdapter.updateData(deckModelArrayList)
                        } else {
                            Log.d("API", "No Tables Received")
                        }
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Log.e("API", "Error: ${t.message}")
                } })
    }
}