import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jmailen.kotlinter") version "4.2.0" // lintKotlin, formatKotlin
    id("com.getkeepsafe.dexcount") version "4.0.0" // :app:countReleaseDexMethods
}

android {
    namespace = "com.cyb3rko.flashdim"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.cyb3rko.flashdim"
        minSdk = 33
        targetSdk = 34
        versionCode = 25
        versionName = "2.3.3"
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
        create("benchmark") {
            initWith(getByName("release"))
            matchingFallbacks.add("release")
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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
            getByName("release") {
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

if (project.hasProperty("gplay_upload")) {
    android {
        signingConfigs {
            getByName("upload") {
                val properties = Properties()
                properties.load(project.rootProject.file("local.properties").inputStream())
                storeFile = file(properties.getProperty("uploadsigning.file"))
                storePassword = properties.getProperty("uploadsigning.password")
                keyAlias = properties.getProperty("uploadsigning.key.alias")
                keyPassword = properties.getProperty("uploadsigning.key.password")
            }
        }
    }
    android.buildTypes.getByName("release").signingConfig = android.signingConfigs.getByName("upload")
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
}

configurations {
    configureEach {
        exclude("androidx.lifecycle", "lifecycle-viewmodel-ktx")
    }
}
