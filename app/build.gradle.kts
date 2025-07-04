@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinter) // lintKotlin, formatKotlin
    alias(libs.plugins.dexcount) // :app:countReleaseDexMethods
    alias(libs.plugins.bundletool)
    alias(libs.plugins.baselineprofile)
}

android {
    namespace = "com.cyb3rko.flashdim"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.cyb3rko.flashdim"
        minSdk = 33
        targetSdk = 36
        versionCode = 30
        versionName = "2.4.1"
        resValue("string", "app_name", "FlashDim Dev")
        buildConfigField("String", "GIT_VERSION", "\"${getGitVersion()}\"")
        signingConfig = signingConfigs.getByName("debug")
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
        }
        release {
            resValue("string", "app_name", "FlashDim")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        // buildType without accessibility services
        register("libre") {
            resValue("string", "app_name", "FlashDim")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules-libre.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    bundle {
        storeArchive {
            enable = false
        }
    }
    androidResources {
        localeFilters += listOf("en")
    }
    packaging {
        resources {
            excludes.add("META-INF/*.version")
            excludes.add("META-INF/**/LICENSE.txt")
        }
    }
    dependenciesInfo {
        // Exclude dependency metadata when building APKs by default
        includeInApk = false
        // Exclude dependency metadata when building AABs by default
        includeInBundle = false
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.material)
    "baselineProfile"(project(":baseline"))
}

configurations {
    configureEach {
        exclude("androidx.lifecycle", "lifecycle-viewmodel-ktx")
    }
}

// Automatic pipeline build
// build with '-Psign assembleRelease'
// output at 'app/build/outputs/apk/release/app-release.apk'
// build with '-Psign bundleRelease'
// output at 'app/build/outputs/bundle/release/app-release.aab'
if (project.hasProperty("sign")) {
    android {
        signingConfigs {
            create("release") {
                enableV3Signing = true
                enableV4Signing = true
                storeFile = file(System.getenv("KEYSTORE_FILE"))
                storePassword = System.getenv("KEYSTORE_PASSWD")
                keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
                keyPassword = System.getenv("KEYSTORE_KEY_PASSWD")
            }
        }
    }
    android.buildTypes.getByName("release").signingConfig =
        android.signingConfigs.getByName("release")
}

// Manual Accrescent build
// build with '-Pmanual_upload_oss buildApksLibre'
// output at 'app/build/outputs/apkset/libre/app-libre.apks'
if (project.hasProperty("manual_upload_oss")) {
    bundletool {
        signingConfig {
            storeFile = file(System.getenv("KEYSTORE_FILE"))
            storePassword = System.getenv("KEYSTORE_PASSWD")
            keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
            keyPassword = System.getenv("KEYSTORE_KEY_PASSWD")
        }
    }
}

// Manual Google Play Store build
// build with '-Pmanual_upload bundleRelease'
// output at 'app/build/outputs/bundle/release/app-release.aab'
if (project.hasProperty("manual_upload")) {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    android {
        signingConfigs {
            create("upload") {
                enableV3Signing = true
                enableV4Signing = true
                storeFile = file(properties.getProperty("uploadsigning.file"))
                storePassword = properties.getProperty("uploadsigning.password")
                keyAlias = properties.getProperty("uploadsigning.key.alias")
                keyPassword = properties.getProperty("uploadsigning.key.password")
            }
        }
        dependenciesInfo {
            // Include dependency metadata when building APKs for Google Play
            includeInApk = true
            // Include dependency metadata when building AABs for Google Play
            includeInBundle = true
        }
    }
    android.buildTypes.getByName("release").signingConfig = android.signingConfigs.getByName("upload")
}

fun getGitVersion(): String {
    val branch = providers.exec {
        commandLine("git", "rev-parse", "--abbrev-ref", "HEAD")
    }.standardOutput.asText.get().trim()
    val hash = providers.exec {
        commandLine("git", "rev-parse", "--short=7", "HEAD")
    }.standardOutput.asText.get().trim()
    return "$branch-$hash"
}
