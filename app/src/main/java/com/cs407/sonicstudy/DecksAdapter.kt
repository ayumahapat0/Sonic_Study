package com.cs407.sonicstudy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DecksAdapter(
    private val context: Context,
    decksModelArrayList: ArrayList<DeckModel>,
    private val itemClickListener: (DeckModel) -> Unit
) : RecyclerView.Adapter<DecksAdapter.ViewHolder>() {

    private val decksModelArrayList: ArrayList<DeckModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DecksAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.deck_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DecksAdapter.ViewHolder, position: Int) {
        // to set data to textview of each card layout
        val model: DeckModel = decksModelArrayList[position]
        holder.deckName.setText(model.get_deck_name())
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return decksModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deckName: TextView
        init {
            deckName = itemView.findViewById(R.id.deckName)
            itemView.setOnClickListener {
                val clickedDeck = decksModelArrayList[adapterPosition]
                itemClickListener(clickedDeck)
            }
        }
    }

    fun updateData(newDeckList: ArrayList<DeckModel>) {
        decksModelArrayList.clear() // Clear old data
        decksModelArrayList.addAll(newDeckList) // Add new data
        notifyDataSetChanged() // Notify the adapter that data has changed
    }

    // Constructor
    init {
        this.decksModelArrayList = decksModelArrayList
    }
}
