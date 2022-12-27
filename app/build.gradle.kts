plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.example.currencycalculator"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.currencycalculator"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    val jodaTimeVersion: String by System.getProperties()
    val junitVersion: String by System.getProperties()
    val mockitoVersion: String by System.getProperties()
    val mockitoKotlinVersion: String by System.getProperties()
    val timberVersion: String by System.getProperties()

    // Modules
    implementation(project(":data"))

    // Support
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")

    // Android Compose
    val composeBom = platform("androidx.compose:compose-bom:2022.11.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Koin
    implementation("io.insert-koin:koin-android:3.3.1")
    implementation("io.insert-koin:koin-androidx-compose:3.4.0")

    // Joda Time
    implementation("net.danlew:android.joda:$jodaTimeVersion")

    // Logging
    implementation("com.jakewharton.timber:timber:$timberVersion")

    // Tests
    testImplementation("junit:junit:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-inline:$mockitoVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("joda-time:joda-time:$jodaTimeVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // UI Tests
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // testImplementation "io.insert-koin:koin-test:$koin_version"
    // testImplementation "io.insert-koin:koin-test-junit4:$koin_version"
}
