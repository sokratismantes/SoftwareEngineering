plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.smartmed1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smartmed1"
        minSdk = 24
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

        // Core KTX gives you handy Kotlin/Java extensions
       implementation("androidx.core:core-ktx:1.9.0")

        // RecyclerView for your survey list
        implementation("androidx.recyclerview:recyclerview:1.2.1")

        // If you end up using CardView or other support‑v4 widgets
        implementation("androidx.cardview:cardview:1.0.0")
         implementation("androidx.legacy:legacy-support-v4:1.0.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
