pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url =
                java.net.URI("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-andorid/")
        }
    }

    versionCatalogs {
        create("libs") {

            version("activity-ktx", "1.8.1")
            version("androidx-junit", "1.1.5")
            version("appcompat", "1.6.1")
            version("constraintlayout", "2.1.4")
            version("core-ktx", "1.12.0")
            version("espresso-core", "3.5.1")
            version("fragment-ktx", "1.6.2")
            version("glide", "4.15.1")
            version("hilt-android", "2.50")
            version("junit", "4.13.2")
            version("kotlinx-coroutines-android", "1.7.1")
            version("kotlinx-serialization-json", "1.6.1")
            version("lifecycle-viewmodel-ktx", "2.6.2")
            version("material", "1.10.0")
            version("navigation-fragment-ktx", "2.7.5")
            version("okhttp", "4.9.1")
            version("retrofit", "2.9.0")
            version("room-compiler", "2.6.0")
            version("timber", "5.0.1")
            version("android-gradle", "8.1.1")
            version("kotlin", "1.9.0")
            version("devtools-ksp", "1.9.10-1.0.13")
            version("kotlinx-serialization", "1.9.20")
            version("kotlinx-serialization-converter", "1.0.0")
            version("firebase-analytics-ktx", "")
            version("firebase-crashlytics-ktx", "")
            version("firebase-crashlytics-gradle", "2.9.9")
            version("firebase-messaging", "23.3.1")
            version("firebase-bom", "32.6.0")
            version("gms-googleServices", "4.4.0")
            version("firebase.appdistribution", "4.0.1")
            version("android-library", "8.1.1")
            version("gms-play-services-auth", "20.7.0")
            version("crypto", "1.0.0-alpha02")
            version("vkid", "1.0.0")


            plugin(
                "android-application",
                "com.android.application"
            ).versionRef("android-gradle")

            plugin(
                "android-kotlin",
                "org.jetbrains.kotlin.android"
            ).versionRef("kotlin")

            plugin(
                "dagger-hilt",
                "com.google.dagger.hilt.android"
            ).versionRef("hilt-android")

            plugin(
                "devtools-ksp",
                "com.google.devtools.ksp"
            ).versionRef("devtools-ksp")

            plugin(
                "org-jetbrains-kotlin-kapt",
                "org.jetbrains.kotlin.kapt"
            ).versionRef("kotlin")

            plugin(
                "kotlinx-serialization",
                "org.jetbrains.kotlin.plugin.serialization"
            ).versionRef("kotlinx-serialization")

            plugin(
                "gms-googleServices",
                "com.google.gms.google-services"
            ).versionRef("gms-googleServices")

            plugin(
                "firebase-crashlytics-gradle",
                "com.google.firebase.crashlytics"
            ).versionRef("firebase-crashlytics-gradle")

            plugin(
                "firebase.appdistribution",
                "com.google.firebase.appdistribution"
            ).versionRef("firebase.appdistribution")

            plugin(
                "android-library",
                "com.android.library"
            ).versionRef("android-library")

            plugin(
                "kotlin-parcelize",
                "org.jetbrains.kotlin.plugin.parcelize"
            ).versionRef("kotlin")

            // Core
            library(
                "androidx-core-ktx",
                "androidx.core",
                "core-ktx"
            ).versionRef("core-ktx")

            library(
                "androidx-appcompat",
                "androidx.appcompat",
                "appcompat"
            ).versionRef("appcompat")

            library(
                "androidx-constraintlayout",
                "androidx.constraintlayout",
                "constraintlayout"
            ).versionRef("constraintlayout")

            library(
                "material",
                "com.google.android.material",
                "material"
            ).versionRef("material")

            // Hilt
            library(
                "hilt-android",
                "com.google.dagger",
                "hilt-android"
            ).versionRef("hilt-android")

            library(
                "hilt-android-compiler",
                "com.google.dagger",
                "hilt-android-compiler"
            ).versionRef("hilt-android")

            // Room
            library(
                "androidx-room-compiler",
                "androidx.room",
                "room-compiler"
            ).versionRef("room-compiler")

            library(
                "androidx-room-ktx",
                "androidx.room",
                "room-ktx"
            ).versionRef("room-compiler")

            library(
                "androidx-room-runtime",
                "androidx.room",
                "room-runtime"
            ).versionRef("room-compiler")

            // Coroutines
            library(
                "kotlinx-coroutines-android",
                "org.jetbrains.kotlinx",
                "kotlinx-coroutines-android"
            ).versionRef("kotlinx-coroutines-android")

            // Navigation Component
            library(
                "androidx-navigation-fragment-ktx",
                "androidx.navigation",
                "navigation-fragment-ktx"
            ).versionRef("navigation-fragment-ktx")

            library(
                "androidx-navigation-ui-ktx",
                "androidx.navigation",
                "navigation-ui-ktx"
            ).versionRef("navigation-fragment-ktx")

            library(
                "androidx-fragment-ktx",
                "androidx.fragment",
                "fragment-ktx"
            ).versionRef("fragment-ktx")

            // Glide
            library(
                "glide",
                "com.github.bumptech.glide",
                "glide"
            ).versionRef("glide")

            library(
                "compiler",
                "com.github.bumptech.glide",
                "compiler"
            ).versionRef("glide")

            // Timber
            library(
                "timber",
                "com.jakewharton.timber",
                "timber"
            ).versionRef("timber")

            // ViewModel + Lifecycle
            library(
                "androidx-lifecycle-viewmodel-ktx",
                "androidx.lifecycle",
                "lifecycle-viewmodel-ktx"
            ).versionRef("lifecycle-viewmodel-ktx")

            library(
                "androidx-activity-ktx",
                "androidx.activity",
                "activity-ktx"
            ).versionRef("activity-ktx")

            // Network
            library(
                "retrofit",
                "com.squareup.retrofit2",
                "retrofit"
            ).versionRef("retrofit")

            library(
                "kotlinx-serialization-json",
                "org.jetbrains.kotlinx",
                "kotlinx-serialization-json"
            ).versionRef("kotlinx-serialization-json")

            library(
                "okhttp",
                "com.squareup.okhttp3",
                "okhttp"
            ).versionRef("okhttp")

            library(
                "logging-interceptor",
                "com.squareup.okhttp3",
                "logging-interceptor"
            ).versionRef("okhttp")

            library(
                "retrofit2-kotlinx-serialization-converter",
                "com.jakewharton.retrofit",
                "retrofit2-kotlinx-serialization-converter"
            ).versionRef("kotlinx-serialization-converter")

            // Test
            library(
                "junit",
                "junit",
                "junit"
            ).versionRef("junit")

            library(
                "androidx-junit",
                "androidx.test.ext",
                "junit"
            ).versionRef("androidx-junit")

            library(
                "androidx-espresso-core",
                "androidx.test.espresso",
                "espresso-core"
            ).versionRef("espresso-core")

            //Firebase
            library(
                "firebase-analytics",
                "com.google.firebase",
                "firebase-analytics-ktx"
            ).withoutVersion()

            library(
                "firebase-crashlytics",
                "com.google.firebase",
                "firebase-crashlytics-ktx"
            ).withoutVersion()

            library(
                "firebase-bom",
                "com.google.firebase",
                "firebase-bom"
            ).versionRef("firebase-bom")

            library(
                "google-services",
                "com.google.gms",
                "google-services"
            ).versionRef("gms-googleServices")

            library("gradle",
                "com.android.tools.build",
                "gradle"
            ).versionRef("android-gradle")

            library("firebase-crashlytics-gradle",
                "com.google.firebase",
                "firebase-crashlytics-gradle"
            ).versionRef("firebase-crashlytics-gradle")

            library(
                "play-services-auth",
                "com.google.android.gms",
                "play-services-auth"
            ).versionRef("gms-play-services-auth")

            library(
                "crypto",
                "androidx.security",
                "security-crypto"
            ).versionRef("crypto")

            // Auth vk
            library("vk-auth",
                "com.vk.id",
                "vkid"
            ).versionRef("vkid")
        }
    }
}

rootProject.name = "Cash Advisor"
include(":app")
include(":uikit")
