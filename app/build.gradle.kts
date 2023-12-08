@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "io.digikraft"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val BIKE_FETCH_URL: String by project
        val BASE_URL: String by project
        buildConfigField("String", "BIKE_FETCH_URL", "\"${BIKE_FETCH_URL}\"")
        buildConfigField("String", "BASE_URL", "\"${BASE_URL}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    implementation("androidx.fragment:fragment:1.5.0")
    implementation("androidx.cardview:cardview:1.0.0")
    //Compose
    implementation("androidx.compose.ui:ui:1.1.1")
    implementation("androidx.compose.material3:material3:1.0.0-alpha14")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.activity:activity-compose:1.6.0-alpha05")
    implementation("androidx.activity:activity-compose:1.5.0")
    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.5.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.23.0")
    // Data (JSON) Parsing
    implementation("com.google.code.gson:gson:2.9.0")
    // Maps and Location
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.maps.android:maps-compose:2.1.0")
    implementation("com.google.android.gms:play-services-location:20.0.0")
    //Dependency Injection
    implementation("com.google.dagger:hilt-android:2.40")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    //Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    kapt("com.google.dagger:hilt-android-compiler:2.40")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.1")
}

secrets {
    // To add your Maps API key to this project:
    // 1. Add this line to your local.properties file, where YOUR_API_KEY is your API key:
    //        MAPS_API_KEY=YOUR_API_KEY
    defaultPropertiesFileName = "local.properties"
}