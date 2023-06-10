import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("com.google.gms.google-services")
}

android {
    namespace = "com.markusw.chatgptapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.markusw.chatgptapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 3
        versionName = "beta 0.13"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
    }

    buildTypes {
        named("release") {
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

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val retrofitVersion = "2.9.0"
    val lifecycleRuntimeVersion = "2.6.0"
    val loggerVersion = "2.2.0"
    val systemUiControllerVersion = "0.30.1"
    val roomVersion = "2.5.0"
    val typistVersion = "1.2.1"
    val splashScreenVersion = "1.0.1"
    val viewModelComposeVersion = "2.5.1"
    val coroutinesTestVersion = "1.7.1"
    val firebaseBomVersion = "32.0.0"
    val mockVersion = "1.13.5"
    val composeVersion = "1.4.3"

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:$firebaseBomVersion"))

    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics-ktx")
    // Firebase remote config
    implementation("com.google.firebase:firebase-config-ktx")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Dagger hilt
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-compiler:2.44.2")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Lifecycle runtime
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleRuntimeVersion")

    // Logger
    implementation("com.orhanobut:logger:$loggerVersion")

    // System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:$systemUiControllerVersion")

    // Room
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Typist type writer effect library
    implementation("tech.dev-scion:typist:$typistVersion")

    // Splash screen API
    implementation("androidx.core:core-splashscreen:$splashScreenVersion")

    // Json encoding
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // ViewModel compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$viewModelComposeVersion")

    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material3:material3:1.1.0")

    // Test implementations
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:$mockVersion")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesTestVersion")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
