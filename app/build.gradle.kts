plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.test.employeepresence"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.test.employeepresence"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.test.employeepresence.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    kotlin {
        //jvmToolchain(8)
    }

    buildFeatures {
        viewBinding = true
    }

    buildToolsVersion = "33.0.1"
}

dependencies {

    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.7.1")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:20.0.0")
    implementation("androidx.lifecycle:lifecycle-service:2.8.0")

    //Room
    implementation("androidx.room:room-common:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.45")
    implementation("androidx.hilt:hilt-common:1.2.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-work:1.2.0")

    // JUnit dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Espresso dependencies for UI testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Room testing dependencies
    testImplementation("androidx.room:room-testing:2.3.0")

    // AndroidX Test - Instrumentation testing
    androidTestImplementation("androidx.test:core:1.3.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test:rules:1.3.0")

    // Coroutine test dependency
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:3.9.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1") // Update to the latest version if necessary

    // Arch core testing dependency
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    // Hilt testing dependencies
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.40.5")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.40.5")
    kaptTest("com.google.dagger:hilt-android-compiler:2.44")

    // AR core dependency
    implementation("com.google.ar:core:1.29.0")

}

kapt {
    correctErrorTypes = true
    arguments {
            arg("room.schemaLocation", "$projectDir/schemas".toString())

    }
}

secrets {
    // To add your Maps API key to this project:
    // 1. Open the root project's local.properties file
    // 2. Add this line, where YOUR_API_KEY is your API key:
    //        MAPS_API_KEY=YOUR_API_KEY
    defaultPropertiesFileName="local.properties"
}