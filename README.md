# Weight Tracker Android App

A simple Android application built during a learning process to track weight entries over time. It demonstrates core Android concepts including UI layouts, data persistence with Room, list display with RecyclerView, basic charting, and background processing with Coroutines.

## Screenshots

*(**TODO:** Add screenshots here!)*

*   *Take a screenshot of the main screen showing the chart and the list.*
*   *Take a screenshot of the "Add New Weight" dialog.*
*   *You can upload images directly to your GitHub repository and link them like this:*
    ```markdown
    ![Main Screen](link/to/your/main_screen_screenshot.png)
    ![Add Dialog](link/to/your/add_dialog_screenshot.png)
    ```

## Features

*   **Add Entries:** Add new weight entries including the weight value and date (selected via a Date Picker).
*   **View List:** Displays all saved weight entries in a scrollable list, sorted by newest first.
*   **View Chart:** Shows a line chart visualizing the weight trend over time.
*   **Edit Entries:** Tap an entry in the list to open a dialog pre-filled with its data, allowing modification of weight and date.
*   **Delete Entries:** Swipe entries left or right in the list to delete them.
*   **Local Storage:** Data is stored persistently on the device using the Room database library.

## Tech Stack & Concepts

*   **Language:** Kotlin
*   **UI:** Android Views (XML Layouts)
    *   ConstraintLayout
    *   RecyclerView
    *   Material Components (Buttons, Dialogs)
*   **Architecture Components:**
    *   ViewModel (using `androidx.activity.viewModels` delegate)
    *   LiveData (for observing data changes)
*   **Database:** Room Persistence Library
*   **Annotation Processing:** KSP (Kotlin Symbol Processing) for Room
*   **Charting:** MPAndroidChart library
*   **Asynchronous Programming:** Kotlin Coroutines (`lifecycleScope`, `viewModelScope`)
*   **Build System:** Gradle with Kotlin DSL (`.kts`) and Version Catalog (`libs.versions.toml`)
*   **Version Control:** Git / GitHub

## Setup & Build

1.  **Prerequisites:**
    *   Android Studio (e.g., Iguana or later recommended)
    *   Git installed
    *   Android SDK installed (via Android Studio SDK Manager)
2.  **Clone the repository:**
    ```bash
    git clone https://github.com/shivam1717tuae/WeightTrackerAndroidApp.git
    ```
    *(Replace with your actual repository URL if different)*
3.  **Open in Android Studio:**
    *   File -> Open -> Navigate to the cloned project directory.
4.  **Gradle Sync:** Let Android Studio sync the project with Gradle (this should happen automatically, or click the Sync button if prompted). It will download necessary dependencies.
5.  **Build/Run:**
    *   Select an emulator or connect a physical Android device.
    *   Click the Run button (green triangle) or go to Run -> Run 'app'.

## Future Enhancements (Ideas)

*   Implement "Undo" functionality for deletions (using a Snackbar).
*   Add unit selection (kg/lbs) with conversion.
*   Improve chart interaction (zoom, pan, highlighting points).
*   Enhance UI/UX styling.
*   Add data validation for reasonable weight ranges.
*   Consider data backup/export options.

*(Consider adding a LICENSE file, e.g., MIT License, if you want others to freely use/modify your code.)*
