plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.envigite.minecraftaplication"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "API_BASE_URL", "\"https://minecraft-api.vercel.app/api/\"")
        applicationId = "com.envigite.minecraftaplication"
        minSdk = 28
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.car.ui.lib)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    dependencies {
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.hilt.android)
        kapt(libs.hilt.compiler)
        implementation(libs.picasso)
        implementation(libs.fastscroll)
        implementation(libs.logging.interceptor)
        implementation(libs.androidx.core.splashscreen)
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        kapt(libs.androidx.room.compiler)
    }
}