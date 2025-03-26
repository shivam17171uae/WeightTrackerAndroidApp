plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
   // alias(libs.plugins.kotlinKapt) // Correct alias (Capital K)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.shivam.weighttracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shivam.weighttracker"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true // This is Kotlin syntax (notice the = sign)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // Room Database - using Version Catalog aliases
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // For Kotlin extensions and Coroutines support
    // --- ViewModel ---
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Check libs.versions.toml for this alias definition
    implementation(libs.androidx.lifecycle.livedata.ktx)  // We'll use LiveData for simplicity here
    implementation(libs.androidx.activity.ktx) // Often needed for viewModels delegate
    implementation(libs.mpandroidchart)
    //kapt(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}