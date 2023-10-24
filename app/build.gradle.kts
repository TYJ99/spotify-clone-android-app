plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")

//    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.tyj.spotifycloneandroidapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tyj.spotifycloneandroidapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
//        jvmTarget = JavaVersion.VERSION_1_8.toString()
//        jvmTarget = "1.8"
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


//    dataBinding {
//        android.buildFeatures.dataBinding = true
//    }
}

//kotlin {
//    jvmToolchain(8)
//}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    //implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation ("androidx.compose.material:material:1.1.0-rc01")
    implementation ("androidx.compose.material:material-icons-extended")

    implementation ("com.google.accompanist:accompanist-permissions:0.30.0")

    // Coroutine
    val coroutinesVersion: String = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
//    implementation("com.google.dagger:hilt-android:2.40.5")
//    kapt ("com.google.dagger:hilt-android-compiler:2.40.5")
//    implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Location Services
    // implementation ("com.google.android.gms:play-services-location:21.0.1")

    /*
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

     */

    /*
    // KotlinX Serialization
    //implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

     */

    // Compose dependencies
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.5.1")

    // FlowRow
    implementation ("com.google.accompanist:accompanist-flowlayout:0.17.0")

    /*
    // Paging 3.0
    val paging_version = "3.2.1"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")
    // implementation("androidx.paging:paging-compose:3.3.0-alpha02")
    implementation ("androidx.paging:paging-compose:1.0.0-alpha14")

     */

    /*
    // Room Database
    val roomVersion: String = "2.5.2"


    // Room and Lifecycle dependencies
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    // kotlin extensions for coroutine support with room
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation ("androidx.room:room-paging:$roomVersion")

     */
    val activityVersion: String = "1.7.2"
    // kotlin extensions for coroutine support with activities
    implementation("androidx.activity:activity-ktx:$activityVersion")

    // Coil
    //implementation("io.coil-kt:coil-compose:1.3.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    ksp("com.github.bumptech.glide:ksp:4.11.0")
//    implementation("com.github.bumptech.glide:glide:4.11.0")
//    kapt("com.github.bumptech.glide:compiler:4.11.0")

    // Timber
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore:24.9.0")

    // Firebase Storage KTX
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")



    // Firebase Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1")

    // media3 - ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.1.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")

    // media: MediaBrowserServiceCompat and MediaBrowserCompat
    implementation("androidx.media:media:1.6.0")

    // MediaSessionConnector
    implementation("androidx.media3:media3-session:1.1.1")

    /*
    // ExoPlayer
    api("com.google.android.exoplayer:exoplayer-core:2.11.8")
    api("com.google.android.exoplayer:exoplayer-ui:2.11.8")
    api("com.google.android.exoplayer:extension-mediasession:2.11.8")

     */

}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}



/*
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tyj.spotifycloneandroidapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tyj.spotifycloneandroidapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

 */