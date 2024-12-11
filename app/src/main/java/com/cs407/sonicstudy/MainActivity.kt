package com.cs407.sonicstudy

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val addTermButton = findViewById<Button>(R.id.button)

        addTermButton.setOnClickListener {
            val intent = Intent(this, AddTerm::class.java)
            startActivity(intent)
        }

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
//                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
                return true
            }
            R.id.study -> {
                showDeckSelectionDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeckSelectionDialog() {
        val decks = arrayOf("Deck 1", "Deck 2", "Deck 3") // Hardcoded deck names
        AlertDialog.Builder(this)
            .setTitle("Choose a Deck to Study")
            .setItems(decks) { _, which ->
                val selectedDeck = decks[which]
                val intent = Intent(this, FlashcardActivity::class.java)
                intent.putExtra("DECK_NAME", selectedDeck)
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}