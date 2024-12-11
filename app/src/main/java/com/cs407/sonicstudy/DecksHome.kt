package com.cs407.sonicstudy

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

typealias DatabaseRow = Map<String, Any>

class DecksHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_decks_home)

            val deckRV = findViewById<RecyclerView>(R.id.RVDecks)
            val tables: ArrayList<String> = ArrayList<String>()

            // Initialize the adapter here so we can update it later
            val deckAdapter = DecksAdapter(this, ArrayList())

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