package com.shivam.weighttracker.adapter // Ensure this matches the package

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shivam.weighttracker.data.WeightEntry
import com.shivam.weighttracker.databinding.ListItemWeightBinding // View Binding for the row layout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Add listener parameter to constructor
class WeightListAdapter(private val listener: OnItemClickListener) : ListAdapter<WeightEntry, WeightListAdapter.WeightViewHolder>(WeightDiffCallback()) {

    // --- Listener Interface ---
    // Define the interface for click events
    interface OnItemClickListener {
        fun onItemClick(entry: WeightEntry)
    }
    // --- End Listener Interface ---


    // --- ViewHolder ---
    // Inner class that holds the views for a single list item
    // No listener parameter needed here anymore
    class WeightViewHolder(private val binding: ListItemWeightBinding) : RecyclerView.ViewHolder(binding.root) {

        // Bind data to the views in the list item layout
        fun bind(entry: WeightEntry) {
            binding.textViewWeightValue.text = "${entry.weight} kg" // Display weight with unit
            binding.textViewDate.text = formatDate(entry.timestamp) // Display formatted date
        }

        // Helper function to format the timestamp into a readable date string
        private fun formatDate(timestamp: Long): String {
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Example format: 26 Mar 2025
            return dateFormat.format(Date(timestamp))
        }

        // Static function to create a ViewHolder instance
        // No listener parameter needed here anymore
        companion object {
            fun from(parent: ViewGroup): WeightViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeightBinding.inflate(layoutInflater, parent, false)
                return WeightViewHolder(binding)
            }
        }
    }
    // --- End ViewHolder ---


    // --- ListAdapter Overrides ---
    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        // No listener needed when creating ViewHolder instance directly
        return WeightViewHolder.from(parent)
    }

    // Called by RecyclerView to display the data at the specified position.
    // This method updates the contents of the ViewHolder to reflect the item at the given position.
    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val currentEntry = getItem(position) // Get the data item for this position
        holder.bind(currentEntry) // Bind the data to the ViewHolder's views

        // --- SET CLICK LISTENER HERE ---
        // Set the click listener on the root view of the item layout (holder.itemView)
        holder.itemView.setOnClickListener {
            listener.onItemClick(currentEntry) // Trigger the callback passed from MainActivity
        }
        // --- END SET CLICK LISTENER ---
    }
    // --- End ListAdapter Overrides ---


    // --- DiffUtil Callback ---
    // Helper class for calculating the difference between two lists, enabling efficient updates.
    class WeightDiffCallback : DiffUtil.ItemCallback<WeightEntry>() {
        // Check if two items represent the same object (usually check unique IDs)
        override fun areItemsTheSame(oldItem: WeightEntry, newItem: WeightEntry): Boolean {
            return oldItem.id == newItem.id
        }

        // Check if the contents of two items are the same (check all data fields)
        override fun areContentsTheSame(oldItem: WeightEntry, newItem: WeightEntry): Boolean {
            return oldItem == newItem // Use data class's default equals()
        }
    }
    // --- End DiffUtil Callback ---
}