plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "com.vlascitchii.shorts_screen"
    compileSdk = 36

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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

    implementation(project(":domain:domain_video"))
    implementation(project(":feature:common_ui"))
    testImplementation(project(":test_common:unit_test"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    //compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.constraint.layout.compose)
    implementation(libs.constraint.layout.xml)
    implementation(libs.window.configuration)

    //viewmodel
    implementation(libs.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    //dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt.compiler)

    //Glide
    implementation(libs.glide)

    //Youtube player
    implementation(libs.youtube.player)

    //Paging
    implementation(libs.paging)
    implementation(libs.paging.compose)

    //unit test
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.test.coroutines)

    // androidTest
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.navigation.compose.testing)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.dexmaker)


    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
}