plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Agrega el complemento de Google Services
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.proyectoevaluable"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyectoevaluable"
        minSdk = 30
        targetSdk = 34
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
}

dependencies {
    // Firebase BoM (gestiona automáticamente las versiones de los SDK de Firebase)
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Firebase Authentication (añadir el producto deseado)
    implementation("com.google.firebase:firebase-auth")

    // Firebase Analytics (opcional, pero recomendable)
    implementation("com.google.firebase:firebase-analytics")

    // Otras dependencias que ya tengas
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}