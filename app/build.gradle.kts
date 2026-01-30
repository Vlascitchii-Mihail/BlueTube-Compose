import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "com.vlascitchii.bluetubecompose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.vlascitchii.bluetubecompose"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.vlascitchii.bluetubecompose.util.BlueTubeTestRunner"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
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
    implementation(project(":feature:settings_screen"))
    implementation(project(":feature:video_list_screen"))
    implementation(project(":feature:shorts_screen"))
    implementation(project(":feature:player_screen"))
    implementation(project(":feature:common_ui"))
    implementation(project(":domain:domain_video"))
    implementation(project(":repository:repository_video"))
    implementation(project(":data:remote_source"))
    implementation(project(":data:local_source"))

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
    implementation(libs.constraint.layout.compose)
    implementation(libs.constraint.layout.xml)
    implementation(libs.window.configuration)

    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt.compiler)
    implementation(libs.hilt.navigation)

    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //viewmodel
    implementation(libs.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    //navigation
    implementation(libs.nav3.runtime)
    implementation(libs.nav3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.nav3)
    implementation(libs.androidx.material3.adaptive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    //Paging
    implementation(libs.paging)
    implementation(libs.paging.compose)

    implementation(libs.coil)

    //Youtube player
    implementation(libs.youtube.player)

    //unit test
    testImplementation(libs.junit)
//    testImplementation(libs.mockito)
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
    androidTestImplementation(libs.navigation.compose.testing)

    //hilt testing
    androidTestImplementation(libs.android.hilt.test)
    kspAndroidTest(libs.ksp.hilt.android.test)

    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.test.manifest)
}