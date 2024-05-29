import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.parcelize)
}

android {
    namespace = "com.appelier.bluetubecompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.appelier.bluetubecompose"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties: Properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    //ui
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material)

    //ViewModel
    implementation(libs.lifecycle.viewmodel.ktx)

    //LiveData
    implementation(libs.livedata)

    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.lifecycle.runtime)

    //dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt.compiler)
    implementation(libs.hilt.livecycle.viewmodel)
    implementation(libs.hilt.navigation)

    //navigation
    implementation(libs.navigation)
    implementation(libs.kotlinx.serialization.json)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp3)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.moshi)
    implementation(libs.moshi.converter)
    ksp(libs.moshi.compiler)
    implementation(libs.interceptor)

    //Paging
    implementation(libs.paging)

    //Glide
    implementation(libs.glide)

    //Youtube player
    implementation(libs.youtube.player)

    //test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.test.manifest)
}