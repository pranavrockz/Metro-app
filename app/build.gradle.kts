plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.quizapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quizapp"
        minSdk = 29
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation ("org.apache.commons:commons-lang3:3.12.0")

    // Lifecycle components
    implementation ("androidx.lifecycle:lifecycle-runtime-android:2.8.3"){
        exclude("androidx.lifecycle", "lifecycle-viewmodel-android" )
        exclude("androidx.lifecycle", "lifecycle-viewmodel-desktop" )
        exclude("androidx.lifecycle" , "lifecycle-runtime-desktop")
    }

    implementation ("androidx.lifecycle:lifecycle-livedata:2.8.3"){
        exclude("androidx.lifecycle", "lifecycle-viewmodel-android" )
        exclude("androidx.lifecycle", "lifecycle-viewmodel-desktop" )
        exclude("androidx.lifecycle" , "lifecycle-runtime-desktop")
    }
    implementation("androidx.lifecycle:lifecycle-viewmodel-android:2.8.3"){
        exclude("androidx.lifecycle", "lifecycle-viewmodel-android")
        exclude("androidx.lifecycle", "lifecycle-viewmodel-desktop" )
        exclude("androidx.lifecycle" , "lifecycle-runtime-desktop")
    }

    // Room Database
    var room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // AndroidX Libraries
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.9.0")

    // Testing dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Other dependencies
    implementation("org.apache.poi:poi:5.3.0")  // Core POI library
    implementation("org.apache.poi:poi-ooxml:5.3.0")  // For OOXML formats (.xlsx)
}