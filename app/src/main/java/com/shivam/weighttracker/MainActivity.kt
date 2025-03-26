package com.shivam.weighttracker // Correct package name based on your project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast // Import the Toast class
import com.shivam.weighttracker.databinding.ActivityMainBinding // Correct import for View Binding
import android.view.LayoutInflater // Add this line
import androidx.appcompat.app.AlertDialog // Add this line
import com.google.android.material.dialog.MaterialAlertDialogBuilder // Add this line
import com.shivam.weighttracker.databinding.DialogAddWeightBinding // Add this line
import androidx.lifecycle.lifecycleScope // To launch coroutines tied to the Activity's lifecycle
import kotlinx.coroutines.launch // To launch a new coroutine
import com.shivam.weighttracker.data.WeightDatabase // Import Database class
import com.shivam.weighttracker.data.WeightEntry // Import Entity class
import java.util.Date // To get the current timestamp
// ViewModel related
import androidx.activity.viewModels // For the 'by viewModels()' delegate
import com.shivam.weighttracker.viewmodel.WeightViewModel // Your ViewModel
import androidx.recyclerview.widget.ItemTouchHelper // Restore import
import androidx.recyclerview.widget.RecyclerView // Import RecyclerView for onMove

// RecyclerView related
import androidx.recyclerview.widget.LinearLayoutManager // To position items in the list
import com.shivam.weighttracker.adapter.WeightListAdapter // Your Adapter

// Charting imports
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter // For formatting axis labels
import java.text.SimpleDateFormat // Already imported, ensure it's there
import java.util.Locale           // Already imported, ensure it's there
import java.util.concurrent.TimeUnit // For converting timestamps
import android.app.DatePickerDialog // For the date picker dialog
import java.util.Calendar // To manage date components
import java.text.DateFormat // For formatting the date on the button

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val weightViewModel: WeightViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- RecyclerView Setup ---
        // Create the adapter, passing the listener implementation
        val adapter = WeightListAdapter(object : WeightListAdapter.OnItemClickListener {
            override fun onItemClick(entry: WeightEntry) {
                // --- HANDLE ITEM CLICK HERE ---
                showAddEditDialog(entry) // Call the dialog function, passing the clicked entry
                // --- END HANDLE ITEM CLICK ---
            }
        })
        val recyclerView = binding.recyclerViewWeights
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        // --- End RecyclerView Setup ---

        // ... SwipeToDetelete, ObserveViewModel ...
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Not used
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // --- CORRECTED WAY TO GET ITEM ---
                if (position != RecyclerView.NO_POSITION) { // Check if position is valid
                    // Get the adapter from the RecyclerView
                    val currentAdapter = (recyclerView.adapter as? WeightListAdapter)
                    currentAdapter?.let { // Safely use the adapter if it's not null
                        val entryToDelete = it.currentList[position]
                        // Call the delete function on the ViewModel
                        weightViewModel.deleteEntry(entryToDelete)
                        // Show feedback
                        Toast.makeText(this@MainActivity, "Entry deleted", Toast.LENGTH_SHORT).show()
                    }
                }
                // --- END CORRECTION ---
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        // --- End Swipe to Delete Setup --- // Restore this block

        // --- Observe ViewModel LiveData ---
        weightViewModel.allEntries.observe(this) { entries ->
            entries?.let {
                adapter.submitList(it) // Update the RecyclerView adapter
                setupChart(it)         // CALL setupChart with the new list
            }
        }
// --- End Observe ViewModel LiveData ---

        // --- FAB Click Listener ---
        // (This block remains as is)
        binding.fabAddWeight.setOnClickListener {
            showAddEditDialog() // Call the new function to show dialog for adding
        }
        // --- End FAB Click Listener ---
    } // End of onCreate

    private fun setupChart(entries: List<WeightEntry>) {
        if (entries.isEmpty()) {
            binding.chartWeight.clear() // Clear chart if no data
            binding.chartWeight.invalidate() // Refresh chart view
            return
        }

        // 1. Convert WeightEntry list to MPAndroidChart Entry list
        // We need a stable reference point for the X-axis (time). Let's use the timestamp of the *first* entry.
        // We sort the list by timestamp ascending to ensure the chart draws left-to-right chronologically.
        val sortedEntries = entries.sortedBy { it.timestamp }
        val firstTimestamp = sortedEntries.first().timestamp

        val chartEntries = sortedEntries.map { weightEntry ->
            // Calculate time difference from the first entry in DAYS for the X-axis value
            val timeDiffMillis = weightEntry.timestamp - firstTimestamp
            val timeDiffDays = TimeUnit.MILLISECONDS.toDays(timeDiffMillis).toFloat()

            // Y-axis value is the weight
            Entry(timeDiffDays, weightEntry.weight.toFloat())
        }

        // 2. Create a DataSet
        val dataSet = LineDataSet(chartEntries, "Weight Trend") // Label for the dataset
        dataSet.color = android.graphics.Color.BLUE // Line color
        dataSet.valueTextColor = android.graphics.Color.BLACK
        dataSet.setCircleColor(android.graphics.Color.BLUE) // Color of data points
        dataSet.circleRadius = 4f
        dataSet.valueTextSize = 10f

        // 3. Create LineData object
        val lineData = LineData(dataSet)

        // 4. Configure Chart Appearance
        binding.chartWeight.description.isEnabled = false // Disable chart description
        binding.chartWeight.legend.isEnabled = true // Show legend (optional)

        // Configure X-axis (Time)
        val xAxis = binding.chartWeight.xAxis
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM // Labels at bottom
        xAxis.granularity = 1f // Minimum interval between labels (1 day)
        xAxis.valueFormatter = object : ValueFormatter() {
            private val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault()) // Format for labels e.g., "Mar 26"
            override fun getAxisLabel(value: Float, axis: com.github.mikephil.charting.components.AxisBase?): String {
                // Convert the days-since-start value back to a timestamp to format the date
                val millis = firstTimestamp + TimeUnit.DAYS.toMillis(value.toLong())
                return dateFormat.format(Date(millis))
            }
        }


        // Configure Y-axis (Weight) - Left
        val leftAxis = binding.chartWeight.axisLeft
        leftAxis.granularity = 1f // Minimum interval (e.g., 1 kg)
        // leftAxis.axisMinimum = 0f // Optional: Set minimum value

        // Disable Right Y-axis
        binding.chartWeight.axisRight.isEnabled = false

        // 5. Load Data into Chart
        binding.chartWeight.data = lineData
        binding.chartWeight.invalidate() // Refresh the chart
    }

    private fun showAddEditDialog(entryToEdit: WeightEntry? = null) { // Takes optional entry
        val dialogBinding = DialogAddWeightBinding.inflate(LayoutInflater.from(this))
        val calendar = Calendar.getInstance() // Use Calendar to manage the selected date

        // --- Initial Setup ---
        val dialogTitle: String
        if (entryToEdit != null) {
            // EDIT Mode: Pre-fill data
            dialogTitle = "Edit Weight Entry"
            dialogBinding.editTextWeight.setText(entryToEdit.weight.toString())
            calendar.timeInMillis = entryToEdit.timestamp // Set calendar to entry's date
        } else {
            // ADD Mode: Use current date
            dialogTitle = "Add New Weight"
            // Calendar is already set to current date by default
        }

        // Function to update the date button text
        fun updateDateButtonText() {
            dialogBinding.buttonDate.text = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)
        }

        updateDateButtonText() // Set initial date text on the button

        // --- Date Picker Setup ---
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateButtonText() // Update button text when date is picked
        }

        dialogBinding.buttonDate.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        // --- End Date Picker Setup ---

        // --- Build and Show Dialog ---
        MaterialAlertDialogBuilder(this)
            .setTitle(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton("Save") { _, _ ->
                val weightString = dialogBinding.editTextWeight.text.toString()
                if (weightString.isNotBlank()) {
                    try {
                        val weightValue = weightString.toDouble()
                        val selectedTimestamp = calendar.timeInMillis // Get timestamp from Calendar

                        // Create or update the WeightEntry
                        val entryToSave = entryToEdit?.copy( // If editing, copy existing entry...
                            weight = weightValue,        // ...and update fields
                            timestamp = selectedTimestamp
                        ) ?: WeightEntry(                 // Otherwise, create new entry
                            weight = weightValue,
                            timestamp = selectedTimestamp
                        )

                        // TODO: We need an UPDATE function in DAO/ViewModel for editing
                        // For now, we'll just use INSERT for both add/edit (will create duplicates on edit)
                        // We will fix this in the next steps!
                        saveEntry(entryToSave) // Call a separate save function

                    } catch (e: NumberFormatException) {
                        Toast.makeText(this, "Please enter a valid weight number", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Weight cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    } // --- End of showAddEditDialog function ---

    // Updated function to handle saving (insert OR update)
    private fun saveEntry(entry: WeightEntry) {
        if (entry.id == 0) {
            // ID is 0, means it's a NEW entry -> Insert
            // Still use lifecycleScope for insert initiated directly from dialog
            val dao = WeightDatabase.getDatabase(applicationContext).weightDao()
            lifecycleScope.launch {
                dao.insert(entry)
                Toast.makeText(this@MainActivity, "Weight saved!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // ID is not 0, means it's an EXISTING entry -> Update
            // Call the ViewModel's update function
            weightViewModel.updateEntry(entry)
            Toast.makeText(this@MainActivity, "Weight updated!", Toast.LENGTH_SHORT).show()
        }
    }
}