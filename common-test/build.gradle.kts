plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }

}
dependencies {
    //coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //unit test
    implementation(libs.junit)
    implementation(libs.mockito)
    implementation(libs.mockito.inline)
    implementation(libs.test.coroutines)
}
