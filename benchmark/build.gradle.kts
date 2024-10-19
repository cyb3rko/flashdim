@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.androidTest)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.cyb3rko.flashdim.benchmark"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        minSdk = 33
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "LOW-BATTERY"
    }

    buildTypes {
        create("benchmark") {
            resValue("string", "app_name", "FlashDim")
            //minifyEnabled true
            //shrinkResources true
            proguardFiles("benchmark-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }
    }

    testOptions {
        managedDevices {
            localDevices {
                create("pixel3Api34") {
                    device = "Pixel 3"
                    apiLevel = 34
                    systemImageSource = "aosp"
                }
            }
        }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.junit)
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}
