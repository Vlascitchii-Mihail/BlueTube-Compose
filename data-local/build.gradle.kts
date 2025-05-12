plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlinx.serialization)
//    alias(libs.plugins.parcelize)
}

android {
    namespace = "com.vlascitchii.data_local"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "com.appelier.bluetubecompose.BlueTubeTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data-repository"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt.compiler)
    implementation(libs.hilt.navigation)

    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.kotlinx.serialization.json)

    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //unit test
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.test.coroutines)

    //android tests
    androidTestImplementation(libs.test.arch.core)
    androidTestImplementation(libs.android.hilt.test)
    kspAndroidTest(libs.ksp.hilt.android.test)
    androidTestImplementation(libs.androidx.test.ext)
    //add opportunity to spy ony object
    androidTestImplementation(libs.dexmaker)
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.test.coroutines)
}
