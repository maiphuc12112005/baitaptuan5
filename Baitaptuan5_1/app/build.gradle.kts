// build.gradle (Module :app)

plugins {
    // CHỈ DÙNG ALIAS (được khuyến nghị khi dùng libs.versions.toml)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.dagger.hilt.android) // Dùng alias cho Hilt plugin
    kotlin("kapt") // Cần thiết cho Kapt, không cần id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.btvn"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.btvn"
        minSdk = 24
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
        // Đảm bảo các dòng này khớp với cấu hình JVM target và là Java 11 trở lên
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
        // Đảm bảo khớp với phiên bản composeCompiler trong libs.versions.toml
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.activity:activity-ktx:1.8.1")

    // *** CẤU HÌNH FIREBASE VÀ GOOGLE SIGN-IN ĐÚNG CÁCH ***
    // DÒNG NÀY PHẢI LUÔN Ở TRÊN CÙNG cho Firebase để quản lý phiên bản
    implementation(platform(libs.firebase.bom))

    // Thêm các thư viện Firebase và loại trừ module gây xung đột (nếu cần thiết)
    // Tốt nhất là bỏ exclude nếu không có lỗi, BOM sẽ lo phần này
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.analytics) // Nếu cần

    // Google Sign-In (Sử dụng version do Firebase BOM quản lý)
    implementation("com.google.android.gms:play-services-auth")

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt - CHỈ DÙNG CÁC ALIAS TỪ LIBS.VERSIONS.TOML (đã xóa các dòng phiên bản cứng)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Material Icons
    implementation(libs.androidx.compose.material.icons.extended)

    // Coil Image Loading
    implementation(libs.coil.compose)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
