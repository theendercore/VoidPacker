plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.detekt)
}

group = "com.theendercore"
version = "0.1.0"

detekt {
    allRules = true
    config.setFrom(file("gradle/detekt/detekt.yml"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.arrow.core)
    implementation(libs.arrow.optics)
    implementation(libs.arrow.fx.coroutines)

    implementation(libs.semver)
    implementation(libs.clikt)

    implementation(libs.log4j.api)
    implementation(libs.log4j.core)
    implementation(libs.log4j.slf4j)


    implementation(libs.kotlinx.serialization)
    implementation(libs.ktoml)
    implementation(libs.compress)


    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}