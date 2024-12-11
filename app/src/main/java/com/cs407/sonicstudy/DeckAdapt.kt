package com.cs407.sonicstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeckAdapt(
    private val decks: MutableList<Pair<String, List<Pair<String, String>>>>, // Deck name and Q&A pairs
    private val onDeckClick: (String) -> Unit
) : RecyclerView.Adapter<DeckAdapt.DeckViewHolder>() {

    inner class DeckViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deckNameTextView: TextView = itemView.findViewById(R.id.deck_name)
        val deckDetailsTextView: TextView = itemView.findViewById(R.id.deck_details)

        fun bind(deckName: String, qaPairs: List<Pair<String, String>>) {
            deckNameTextView.text = deckName
            deckDetailsTextView.text = qaPairs.joinToString("\n") { "Q: ${it.first}, A: ${it.second}" }
            itemView.setOnClickListener { onDeckClick(deckName) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_preview, parent, false)
        return DeckViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val (deckName, qaPairs) = decks[position]
        holder.bind(deckName, qaPairs)
    }

    override fun getItemCount(): Int = decks.size

    fun addDeck(deckName: String, qaPairs: List<Pair<String, String>>) {
        decks.add(deckName to qaPairs)
        notifyItemInserted(decks.size - 1)
    }
}
