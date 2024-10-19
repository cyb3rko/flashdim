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
    compileSdk = 34
    defaultConfig {
        applicationId = "com.cyb3rko.flashdim"
        minSdk = 33
        targetSdk = 35
        versionCode = 26
        versionName = "2.3.4"
        resValue("string", "app_name", "FlashDim Dev")
        resourceConfigurations.add("en")
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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
    packaging {
        resources {
            excludes.add("META-INF/*.version")
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

if (project.hasProperty("sign")) {
    android {
        signingConfigs {
            create("release") {
                storeFile = file(System.getenv("KEYSTORE_FILE"))
                storePassword = System.getenv("KEYSTORE_PASSWD")
                keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
                keyPassword = System.getenv("KEYSTORE_KEY_PASSWD")
            }
        }
    }
    android.buildTypes.getByName("release").signingConfig =
        android.signingConfigs.getByName("release")

    bundletool {
        signingConfig {
            storeFile = file(System.getenv("KEYSTORE_FILE"))
            storePassword = System.getenv("KEYSTORE_PASSWD")
            keyAlias = System.getenv("KEYSTORE_KEY_ALIAS")
            keyPassword = System.getenv("KEYSTORE_KEY_PASSWD")
        }
    }

}

// Manual Google Play Store and Accrescent builds
if (project.hasProperty("manual_upload")) {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    android {
        signingConfigs {
            create("upload") {
                storeFile = file(properties.getProperty("uploadsigning.file"))
                storePassword = properties.getProperty("uploadsigning.password")
                keyAlias = properties.getProperty("uploadsigning.key.alias")
                keyPassword = properties.getProperty("uploadsigning.key.password")
            }
        }
    }
    android.buildTypes.getByName("release").signingConfig = android.signingConfigs.getByName("upload")

    bundletool {
        signingConfig {
            storeFile = file(properties.getProperty("uploadsigning.file"))
            storePassword = properties.getProperty("uploadsigning.password")
            keyAlias = properties.getProperty("uploadsigning.key.alias")
            keyPassword = properties.getProperty("uploadsigning.key.password")
        }
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
