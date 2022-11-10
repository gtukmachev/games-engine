plugins {
    kotlin("js") version "1.7.20"
}

group = "tga.kjs"
version = "1.0-SNAPSHOT"

repositories {
    // for tga.gaming.engine
    mavenCentral()

    // for tga.gaming.game
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
    // for tga.gaming.engine
    testImplementation(kotlin("test"))

    // for tga.gaming.game
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.2")
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}
