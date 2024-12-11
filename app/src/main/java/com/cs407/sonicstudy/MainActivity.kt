package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.appcompat.widget.Toolbar;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.138.129.173:8080" // Replace with your server IP

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val deckRV = findViewById<RecyclerView>(R.id.home_RVDecks)
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
                        Toast.makeText(this@MainActivity, "Received Decks!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "No Decks currently stored! Start By Adding a Deck!", Toast.LENGTH_SHORT).show()
                        Log.d("API", "No Tables Received")
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error Receiving Decks. Try Again!", Toast.LENGTH_SHORT).show()
                Log.e("API", "Error: ${t.message}")
            } })

    }

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
            R.id.settings -> {
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
                return true
            }
            R.id.study -> {
                val intent = Intent(this, SelectDeck::class.java)
                startActivity(intent)
                true
            }
            R.id.action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}