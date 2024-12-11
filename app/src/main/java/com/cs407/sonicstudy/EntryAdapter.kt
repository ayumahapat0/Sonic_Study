package com.cs407.sonicstudy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntryAdapter (
    private val context: Context,
    entryModelArrayList: ArrayList<EntryModel>,
    private val itemClickListener: (EntryModel) -> Unit
) : RecyclerView.Adapter<EntryAdapter.ViewHolder>() {

    private val entryModelArrayList: ArrayList<EntryModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryAdapter.ViewHolder {
        // to inflate the layout for each item of recycler view.
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.entry_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryAdapter.ViewHolder, position: Int) {
        // to set data to textview of each card layout
        val model: EntryModel = entryModelArrayList[position]
        holder.entryText.setText("${model.get_term()} : " + "${model.get_definition()}")
    }

    override fun getItemCount(): Int {
        // this method is used for showing number of card items in recycler view.
        return entryModelArrayList.size
    }

    // View holder class for initializing of your views such as TextView.
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val entryText: TextView
        init {
            entryText = itemView.findViewById(R.id.entryText)
            itemView.setOnClickListener {
                val clickedTerm = entryModelArrayList[adapterPosition]
                itemClickListener(clickedTerm)
            }
        }
    }

    fun updateData(newEntryList: ArrayList<EntryModel>) {
        entryModelArrayList.clear() // Clear old data
        entryModelArrayList.addAll(newEntryList) // Add new data
        notifyDataSetChanged() // Notify the adapter that data has changed
    }

    // Constructor
    init {
        this.entryModelArrayList = entryModelArrayList
    }
}
