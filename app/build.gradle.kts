// old
@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id("com.google.devtools.ksp") version "1.8.21-1.0.11" // old
    id("com.google.devtools.ksp") version "1.9.22-1.0.16" // new
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.prikol"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
//        kotlinCompilerExtensionVersion = "1.4.7" // old
        kotlinCompilerExtensionVersion = "1.5.8" // new
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.example.prikol"
}

dependencies {
    // Import the Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.06.01"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.6.0")

    //Room
    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    implementation("androidx.core:core-ktx:1.10.1")
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")

    // Testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Accompanist
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("com.google.accompanist:accompanist-webview:0.31.3-beta")
}

// mixed from old and new
////@file:Suppress("UnstableApiUsage")
//
//plugins {
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
////    id("com.google.devtools.ksp") version "1.8.21-1.0.11" // old
//    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
//}
//
//android {
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.example.prikol"
//        minSdk = 26
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    kotlinOptions {
//        jvmTarget = "17"
//    }
//    buildFeatures {
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.8"
//    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//    namespace = "com.example.prikol"
//}
//
//dependencies {
//    // Import the Compose BOM
//    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    implementation("androidx.activity:activity-compose:1.8.2")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//    implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")
//    implementation("androidx.navigation:navigation-compose:2.7.6")
//
//    //Room
//    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
//    implementation("androidx.core:core-ktx:1.12.0")
//    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
//    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
////    implementation("androidx.room:room-common:2.6.1")
//
//    // Testing
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
////    testImplementation("junit:junit:4.13.2")
////    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
////    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
////    debugImplementation("androidx.compose.ui:ui-tooling")
////    debugImplementation("androidx.compose.ui:ui-test-manifest")
//}

// new, suggested
//plugins {
//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
//}
//
//android {
//    namespace = "com.example.prikol"
//    compileSdk = 34
//
//    defaultConfig {
//        applicationId = "com.example.prikol"
//        minSdk = 26
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//    buildFeatures {
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
//    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//}
//
//dependencies {
//
//    implementation("androidx.core:core-ktx:1.12.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
//    implementation("androidx.activity:activity-compose:1.8.2")
//    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.room:room-common:2.6.1")
//    implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")
//    implementation("androidx.navigation:navigation-compose:2.7.6")
//    implementation("androidx.room:room-ktx:2.6.1")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
//
//}