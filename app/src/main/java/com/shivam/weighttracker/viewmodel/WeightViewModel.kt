package com.shivam.weighttracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope // Ensure this is imported
import kotlinx.coroutines.launch // Ensure this is imported
import com.shivam.weighttracker.data.WeightDatabase
import com.shivam.weighttracker.data.WeightEntry

class WeightViewModel(application: Application) : AndroidViewModel(application) {

    private val weightDao = WeightDatabase.getDatabase(application).weightDao()

    val allEntries: LiveData<List<WeightEntry>> = weightDao.getAllEntries().asLiveData()

    fun deleteEntry(entry: WeightEntry) {
        viewModelScope.launch {
            weightDao.delete(entry)
        }
    }

    // --- Add Update Function ---
    /**
     * Launching a new coroutine to update the data in a non-blocking way
     */
    fun updateEntry(entry: WeightEntry) {
        viewModelScope.launch { // Use viewModelScope
            weightDao.update(entry) // Call the DAO's update function
        }
    }
    // --- End Update Function ---
}