import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
}

android {
    namespace = "com.teste.crudapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.teste.crudapplication"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4-alpha07")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
        because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
        because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
    }

    implementation("com.google.code.gson:gson:2.8.7")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.0")
    implementation("androidx.navigation:navigation-ui:2.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}