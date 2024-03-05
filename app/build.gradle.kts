import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.google.firebase.appdistribution.gradle.firebaseAppDistribution
import com.android.build.api.dsl.ApplicationDefaultConfig

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.firebase.crashlytics.gradle)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "app.cashadvisor"
    compileSdk = 34

    defaultConfig {
        applicationId = "app.cashadvisor"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        initVKID()
    }

    signingConfigs {
        create("release") {

            val localProperties = gradleLocalProperties(rootDir)

            val storePasswordLocal: String =
                System.getenv("STORE_PASSWORD") ?: localProperties.getProperty("storePassword")
                ?: "storePasswordEmpty"
            val keyAliasLocal: String =
                System.getenv("KEY_ALIAS") ?: localProperties.getProperty("keyAlias")
                ?: "keyAliasEmpty"
            val keyPasswordLocal: String =
                System.getenv("KEY_PASSWORD") ?: localProperties.getProperty("keyPassword")
                ?: "keyPasswordEmpty"

            storeFile = file("keyStore/cashadvisor.jks")
            storePassword = storePasswordLocal
            keyAlias = keyAliasLocal
            keyPassword = keyPasswordLocal
        }

        getByName("debug") {
            storeFile = file("keyStore/debug.keystore")
            storePassword = "android"
            keyAlias ="androiddebugkey"
            keyPassword = "android"
        }
    }


    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            matchingFallbacks += listOf("release")
            buildConfigField("String", "LOGGING_LEVEL", "\"DEBUG\"")
        }

        create("qa") {
            initWith(getByName("release"))
            applicationIdSuffix = ".qa"
            signingConfig = signingConfigs.getByName("release")
            matchingFallbacks += listOf("release")
            buildConfigField("String", "LOGGING_LEVEL", "\"QA\"")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String", "LOGGING_LEVEL", "\"RELEASE\"")
        }
    }

    flavorDimensions += "env"

    productFlavors {
        create("stage") {
            dimension = "env"
            applicationIdSuffix = ".stage"
        }

        create("prod") {
            dimension = "env"
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
        viewBinding = true
        buildConfig = true
    }
}

firebaseAppDistribution {
    artifactType = "APK"
    releaseNotesFile = "app/src/releaseNotes.txt"
    testers = "QA"
    serviceCredentialsFile = "app/serviceCredentialsFile.json"
}

task("appDistributionToQaStageQa") {
    dependsOn("assembleStageQa")
    dependsOn("appDistributionUploadStageQa")
}

task("appDistributionToQaProdQa") {
    dependsOn("assembleProdQa")
    dependsOn("appDistributionUploadProdQa")
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // Timber
    implementation(libs.timber)

    // ViewModel + Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)

    // Network
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Firebase
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(platform(libs.firebase.bom))

    // Auth vk
    implementation(libs.vk.auth)

    // Ui Kit Library
    implementation(project(":uikit"))

    //Sign-In
    implementation(libs.play.services.auth)

    //Crypto
    implementation(libs.crypto)

}

kapt {
    correctErrorTypes = true
}

fun ApplicationDefaultConfig.initVKID() {
    val localProperties = gradleLocalProperties(rootDir)

    val clientId = System.getenv("VKIDCLIENTID") ?: localProperties.getProperty("VKIDCLIENTID")
        ?: throw GradleException("There is no VKIDClientID. Please specify it by ENV.VKIDCLIENTID or VKIDCLIENTID=xxx in local.properties")
    val clientSecret =
        System.getenv("VKIDCLIENTSECRET") ?: localProperties.getProperty("VKIDCLIENTSECRET")
        ?: throw GradleException("There is no VKIDClientSecret. Please specify it by ENV.VKIDCLIENTSECRET or VKIDCLIENTSECRET=xxx in local.properties")
    addManifestPlaceholders(
        mapOf(
            "VKIDRedirectHost" to "vk.com",
            "VKIDRedirectScheme" to "vk$clientId",
            "VKIDClientID" to clientId,
            "VKIDClientSecret" to clientSecret
        )
    )
}