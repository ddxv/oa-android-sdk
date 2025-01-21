import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.library")
    id("com.vanniktech.maven.publish") version "0.30.0"
}

android {
    namespace = "dev.openattribution"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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


}

dependencies {

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)

    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.play.services.ads.identifier)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}



mavenPublishing {

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("dev.openattribution", "open-attribution-sdk", libs.versions.oaReleaseVersion.get())

    pom {
        name.set("Open Attribution Android SDK")
        description.set("This is the Android SDK for Open Attribution.")
        inceptionYear.set("2024")
        url.set("https://github.com/OpenAttribution/open-attribution/")
        licenses {
            license {
                name.set("MIT license")
                url.set("https://raw.githubusercontent.com/OpenAttribution/oa-android-sdk/refs/heads/main/LICENSE")
                distribution.set("https://raw.githubusercontent.com/OpenAttribution/oa-android-sdk/refs/heads/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("OpenAttribution")
                name.set("Open Attribution")
                url.set("https://github.com/OpenAttribution/")
            }
        }
        scm {
            url.set("https://github.com/OpenAttribution/open-attribution/")
            connection.set("scm:git:git://github.com/OpenAttribtuion/open-attribution.git")
            developerConnection.set("scm:git:ssh://git@github.com/OpenAttribution/open-attribution.git")
        }
    }
}