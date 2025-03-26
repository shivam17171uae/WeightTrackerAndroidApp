package com.shivam.weighttracker.data // Ensure this matches the package

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_table") // Tell Room this is a table
data class WeightEntry(
    @PrimaryKey(autoGenerate = true) // Make id the primary key, automatically generated
    val id: Int = 0, // Unique ID for each entry

    val weight: Double, // The weight value entered by the user

    val timestamp: Long // When the entry was created (we'll use milliseconds)
)