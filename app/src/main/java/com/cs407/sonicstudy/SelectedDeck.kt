package com.cs407.sonicstudy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectedDeck : AppCompatActivity() {
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
        setContentView(R.layout.activity_selected_deck)

        val deckTitle: String = intent.getStringExtra("SelectedDeck").toString()


        findViewById<TextView>(R.id.selected_deck_name).text = deckTitle

        val entryRV = findViewById<RecyclerView>(R.id.RVTerms)

        val tableRows: ArrayList<Map<String, Any>> = ArrayList<Map<String, Any>>()

        // Initialize the adapter here so we can update it later
        val entryAdapter = EntryAdapter(this, ArrayList()) { clickedTerm ->
            Toast.makeText(this, "Clicked on: ${clickedTerm.get_term()}", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ModifyDeleteTerm::class.java)
            intent.putExtra("title", deckTitle)
            intent.putExtra("id", clickedTerm.get_id())
            startActivity(intent)
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
                            val id = row.get("id")
                            entryModelArrayList.add(EntryModel(term.toString(), definition.toString(), id.toString()))
                            Log.d("API", "Row: ${row.get("question")}")
                            Log.d("API", "Row: ${row.get("answer")}")
                            Log.d("API", "Row: ${row.get("id")}")
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