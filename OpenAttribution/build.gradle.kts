plugins {
//    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.android.library")
    `maven-publish`
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

    publishing {
            multipleVariants("MINAMEEE") {
                allVariants()
                withJavadocJar()
            }


        }

    group = "dev.openattribution"
    version = "0.1"

}

dependencies {

    implementation(platform(libs.okhttp.bom))

    implementation(libs.okhttp)

    implementation(libs.play.services.ads.identifier)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

publishing {

    publications {
        register<MavenPublication>("release") {
            groupId = "dev.openattribution"
            artifactId = "my-library"
            from(components["java"])

            version = "0.22"



            pom {
                name.set("POM_NAME")
                url.set("POM_URL")
                description.set("POM_DESCRIPTION")
                developers {
                    developer {
                        id.set("POM_DEVELOPER_ID")
                        name.set("POM_DEVELOPER_NAME")
                    }
                }
                licenses {
                    license {
                        name.set("POM_LICENCE_NAME")
                        url.set("POM_LICENCE_URL")
                        distribution.set("POM_LICENCE_DIST")
                    }
                }

                scm {
                    url.set("POM_SCM_URL")
                    connection.set("POM_SCM_CONNECTION")
                    developerConnection.set("POM_SCM_DEV_CONNECTION")
                }

                // MAYBE NEEDS TO BE IN AFTEREVAL
//                withXml {
//                    fun groovy.util.Node.getChild(name: String): groovy.util.Node {
//                        return (get(name) as groovy.util.NodeList).first() as groovy.util.Node
//                    }
//
//                    fun groovy.util.Node.getChildOrNull(name: String): groovy.util.Node? {
//                        return (get(name) as groovy.util.NodeList).firstOrNull() as? groovy.util.Node
//                    }
//
//                    val node = asNode()
//                    val dependencies = node.getChild("dependencies")
//                    dependencies.children().filterIsInstance<groovy.util.Node>()
//                        .forEach { dependency ->
//                            val artifactId = dependency.getChild("artifactId")
//                            if (artifactId.text() == "okhttp" || artifactId.text() == "firebase-appindexing") {
//                                // Ensure optional flag is set
//                                val optional = dependency.getChildOrNull("optional")
//                                if (optional != null) {
//                                    optional.setValue("true")
//                                } else {
//                                    dependency.appendNode("optional", "true")
//                                }
//
//                                // Ensure scope is set to 'compile'
//                                val scope = dependency.getChildOrNull("scope")
//                                if (scope != null) {
//                                    scope.setValue("compile")
//                                } else {
//                                    dependency.appendNode("scope", "compile")
//                                }
//                            }
//                        }
//                }
            }






        }
    }
}