plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //**DBRoom**
    id("com.google.devtools.ksp")
    //**DBRoom**
}

android {
    namespace = "com.bagha.echomemory"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bagha.echomemory"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.1"

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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //___gif image
    implementation ("io.coil-kt:coil:2.4.0")
    implementation ("io.coil-kt:coil-gif:2.4.0") // برای پشتیبانی از GIF
    //___gif image

    //***********DB_Room
    //implementation ("androidx.room:room-ktx:2.5.0")
    //ksp("androidx.room:room-compiler:2.5.0")
    //implementation ("androidx.room:room-runtime:2.2.5")
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    ksp("androidx.room:room-compiler:2.5.0")
    //***********DB_Room

    //***********YoYo_Animation
    implementation ("com.daimajia.androidanimations:library:2.4@aar")
    //***********YoYo_Animation


}