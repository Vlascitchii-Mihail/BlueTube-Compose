plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin)
}

android {
    namespace = "com.vlascitchii.common_test"
    compileSdk = 36

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(project(":presentation-common"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    implementation(libs.paging)
    implementation(libs.androidx.paging.common)
    implementation(libs.paging.compose)

    //Paging test
    implementation(libs.paging.test)

    //unit test
    implementation(libs.junit)
    implementation(libs.mockito)
    implementation(libs.mockito.inline)
    implementation(libs.test.coroutines)
    implementation(libs.androidx.recyclerview)
}
