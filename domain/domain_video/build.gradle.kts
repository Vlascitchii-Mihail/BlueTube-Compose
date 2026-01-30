plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin)
}

//will be attached to the JVM during tests manually
val mockitoAgent by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
    extendsFrom(configurations.testRuntimeOnly.get())
}

android {
    namespace = "com.vlascitchii.domain_video"
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

    testImplementation(project(":test_common:unit_test"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Paging
    implementation(libs.paging)
    implementation(libs.paging.compose)

    //unit test
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.test.coroutines)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)

    //add the agent to the configuration
    mockitoAgent(libs.mockito.core) {
        isTransitive = false
    }
}

//attache the agent from the configuration
tasks.withType<Test>().configureEach {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}
