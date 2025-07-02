plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    id("com.google.devtools.ksp") version "2.0.0-1.0.24"
}

android {
    namespace = "com.example.frontend"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.frontend"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.window)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.datetime)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.compose.material3)
    ksp(libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.symbol.processing.api)
    implementation(libs.coil.compose)
    implementation(libs.glide)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.core)
    implementation(libs.gson)
    implementation(libs.androidx.paging.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
//Gson

     implementation(libs.gson)
     implementation(libs.coil.compose)     // Để hiển thị ảnh từ URL
     implementation(libs.okhttp) // Để gửi file lên backend
     implementation(libs.androidx.lifecycle.viewmodel.compose)

     implementation(libs.retrofit)
     implementation(libs.converter.gson)
     implementation(libs.logging.interceptor)
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.32.0")
    implementation ("org.threeten:threetenbp:1.6.5") // Hoặc phiên bản mới nhất
    implementation("androidx.activity:activity-compose:1.8.0")
//     ksp(libs.hilt.compiler)


//     implementation (libs.androidx.core.ktx)
//     implementation(libs.androidx.foundation)
//     implementation(libs.androidx.runtime)
//     implementation(libs.ui)


//     implementation("androidx.navigation:navigation-compose:2.8.9")
//     implementation("androidx.datastore:datastore-preferences:1.0.0")
//     implementation("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")

//     implementation (libs.androidx.hilt.navigation.compose)
//     implementation (libs.hilt.android)
//     implementation (libs.androidx.navigation.compose)
//     implementation (libs.androidx.paging.runtime)
//     implementation (libs.androidx.material.icons.extended)

//     ksp(libs.hilt.compiler)
//     // Optional - nếu dùng ViewModel với Hilt
//     implementation(libs.androidx.hilt.navigation.compose)

//     implementation("com.github.bumptech.glide:glide:4.13.0")
//     implementation("com.google.code.gson:gson:2.10.1")
//     implementation("io.coil-kt:coil-compose:2.2.2")
//     implementation ("androidx.compose.foundation:foundation:1.5.0")
//     implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.x.x")
//     implementation("androidx.compose.runtime:runtime-livedata:x.x.x")
//     implementation("com.google.accompanist:accompanist-pager:0.28.0")
//     implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")
//     implementation ("androidx.navigation:navigation-compose:2.7.7")
//     implementation ("com.google.accompanist:accompanist-pager:0.30.1")
//     implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")
//     implementation("androidx.compose.ui:ui:1.5.0")
//     implementation ("androidx.compose.foundation:foundation-layout:1.6.0")



//     implementation("com.google.accompanist:accompanist-flowlayout:0.30.1")

//     implementation ("com.google.zxing:core:3.5.3")

//     // Retrofit và Gson (để parse JSON)
//     implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//     implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
// // Coroutine support
//     implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
// // Gson
//     implementation ("com.google.code.gson:gson:2.10.1")

//     //
//
//    implementation ("com.google.dagger:hilt-android:2.44")
//    kapt("com.google.dagger:hilt-android-compiler:2.44")
//    implementation ("androidx.datastore:datastore-preferences:1.0.0")
}