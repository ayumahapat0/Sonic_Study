package com.cs407.sonicstudy

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectedDeck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_deck)

        val deckTitle: String = intent.getStringExtra("SelectedDeck").toString()

        findViewById<TextView>(R.id.selected_deck_name).text = deckTitle

        val entryRV = findViewById<RecyclerView>(R.id.RVTerms)

        val tableRows: ArrayList<Map<String, Any>> = ArrayList<Map<String, Any>>()

        // Initialize the adapter here so we can update it later
        val entryAdapter = EntryAdapter(this, ArrayList()) { clickedTerm ->
            Toast.makeText(this, "Clicked on: ${clickedTerm.get_term()}", Toast.LENGTH_SHORT).show()
        }

        // Set up RecyclerView with LayoutManager and empty adapter first
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        entryRV.layoutManager = linearLayoutManager
        entryRV.adapter = entryAdapter

        val request = DataModels.RetrieveDataRequest(
            tableName = deckTitle
        )

        // API call to retrieve table data
        RetrofitClient.apiService.retrieveData(request).enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(
                call: Call<List<Map<String, Any>>>,
                response: Response<List<Map<String, Any>>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        tableRows.clear() // Clear any old data
                        tableRows.addAll(data) // Add new data from API
                        Log.d("API", "Tables Received: $tableRows")
                        Log.d("API", "Type: ${data::class.simpleName}")

                        val entryModelArrayList = ArrayList<EntryModel>()

                        for(row in data) {
                            val term = row.get("question")
                            val definition = row.get("answer")
                            entryModelArrayList.add(EntryModel(term.toString(), definition.toString()))
                            Log.d("API", "Row: ${row.get("question")}")
                            Log.d("API", "Row: ${row.get("answer")}")
                        }

                        // Update adapter with the new data
                        entryAdapter.updateData(entryModelArrayList)
                    } else {
                        Log.d("API", "No Tables Received")
                    }
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Log.e("API", "Failure: ${t.message}")
            }
        })

    }
}