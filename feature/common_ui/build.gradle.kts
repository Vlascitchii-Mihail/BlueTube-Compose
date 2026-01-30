plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.kotlinx.serialization)
}

//will be attached to the JVM during tests manually
val mockitoAgent by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
    extendsFrom(configurations.testRuntimeOnly.get())
}

android {
    namespace = "com.vlascitchii.common_ui"
    compileSdk = 36

    defaultConfig {
        minSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    //choose only dexmaker in android test instead both mockito-inline and dexmaker
    packaging {
        resources {
            pickFirsts += "mockito-extensions/org.mockito.plugins.MockMaker"
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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
    testImplementation(project(":test_common:unit_test"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
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
    implementation(libs.icons.core)
    implementation(libs.icons.extended)

    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //dagger hilt
    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt.compiler)
    implementation(libs.hilt.navigation)

    //Paging
    implementation(libs.paging)
    implementation(libs.paging.compose)

    implementation(libs.coil)

    //navigation
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    //unit test
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.test.coroutines)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.navigation.compose.testing)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.mockito)
    androidTestImplementation(libs.dexmaker)

    //add the agent to the configuration
    mockitoAgent(libs.mockito.core) {
        isTransitive = false
    }
}

//attache the agent from the configuration
tasks.withType<Test>().configureEach {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}
