package com.shivam.weighttracker.data // Ensure this matches the package

import android.content.Context // Needed to get the application context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotate the class to be a Room database, declare the entities, and set the version
@Database(entities = [WeightEntry::class], version = 1, exportSchema = false)
abstract class WeightDatabase : RoomDatabase() {

    // Define an abstract function that returns the DAO
    abstract fun weightDao(): WeightDao

    // --- Companion Object for Singleton Pattern ---
    companion object {
        // @Volatile: Ensures that the value of INSTANCE is always up-to-date and the same for all execution threads.
        // It means that changes made by one thread to INSTANCE are visible to all other threads immediately.
        @Volatile
        private var INSTANCE: WeightDatabase? = null

        // Function to get the database instance (Singleton pattern)
        fun getDatabase(context: Context): WeightDatabase {
            // Return the existing instance if it exists
            // Otherwise, create the database inside a synchronized block to prevent multiple threads
            // from creating the database simultaneously.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Use application context
                    WeightDatabase::class.java, // The database class
                    "weight_database"           // Name of the database file
                )
                    // You can add database migrations here later if you change the schema
                    .build() // Build the database instance
                INSTANCE = instance // Assign the newly created instance
                instance // Return the instance
            }
        }
    }
    // --- End Companion Object ---
}