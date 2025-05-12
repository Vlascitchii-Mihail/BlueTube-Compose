import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
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
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
    testOptions {
        emulatorControl {
            enable = true
        }
    }
}

dependencies {
    implementation(project(":presentation-settings"))
    implementation(project(":presentation-video-list"))
    implementation(project(":presentation-shorts"))
    implementation(project(":presentation-player"))
    implementation(project(":presentation-common"))
    implementation(project(":domain"))
    implementation(project(":data-repository"))
    implementation(project(":data-remote"))
    implementation(project(":data-local"))

    //core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    //ui
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material)
    implementation(libs.constraint.layout.compose)
    implementation(libs.constraint.layout.xml)
    implementation(libs.window.configuration)

    //ViewModel
//    implementation(libs.lifecycle.viewmodel.ktx)
//    implementation(libs.viewmodel.compose)

    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt.compiler)
    implementation(libs.hilt.navigation)

    //navigation
    implementation(libs.navigation)
    implementation(libs.kotlinx.serialization.json)

    //unit test
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.test.coroutines)

//    android tests
    androidTestImplementation(libs.test.arch.core)
    //add opportunity to spy ony object
    androidTestImplementation(libs.dexmaker)
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
//    androidTestImplementation(libs.navigation.compose.testing)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.test.manifest)
}