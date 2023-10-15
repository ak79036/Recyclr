plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.wastemangement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wastemangement"
        minSdk = 28
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }
    androidResources {
        noCompress ("tflite")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    ///  messaging
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation ("org.chromium.net:cronet-api:113.5672.61")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0" )
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//courotines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //lottie files
    implementation("com.airbnb.android:lottie:6.1.0")
    //mapBox
    implementation ("com.mapbox.maps:android:10.16.0")
    implementation ("com.mapbox.navigation:android:2.16.0")
    implementation ("com.mapbox.search:mapbox-search-android:1.0.0-rc.6")
    //firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation ("com.jpardogo.googleprogressbar:library:1.2.0")

    // CameraX core library




    implementation ("androidx.camera:camera-core:1.4.0-alpha01")

    // CameraX Camera2 extensions
    implementation ("androidx.camera:camera-camera2:1.4.0-alpha01")

    // CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:1.4.0-alpha01")

    // CameraX View class
    implementation ("androidx.camera:camera-view:1.4.0-alpha01")

    //WindowManager
    implementation ("androidx.window:window:1.2.0-beta03")

    implementation ("org.tensorflow:tensorflow-lite-task-vision-play-services:0.4.2")
    implementation ("com.google.android.gms:play-services-tflite-gpu:16.2.0")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // cloudinary
    implementation ("com.cloudinary:cloudinary-android:2.3.1")
    implementation ("com.cloudinary:cloudinary-core:1.24.0")

    //coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")  // Replace with the latest version
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")  // Replace with the latest version
    implementation ("androidx.room:room-ktx:2.3.0")  // Replace with the latest version
    implementation ("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")  // Replace with the latest version

}