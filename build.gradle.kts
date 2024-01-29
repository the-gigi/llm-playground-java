plugins {
    java
    application
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.21"
}

group = "com.github.the.gigi"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.guava)
    implementation(libs.jackson.databind)
    implementation(libs.langchain4j)
    implementation(libs.langchain4j.open.ai)
    implementation(libs.okhttp)
    implementation(libs.openai.java.api)
    implementation(libs.openai.java.client)
    implementation(libs.openai.java.service)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.jackson)
    implementation(libs.openai.kotlin.client)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.simple.openai)

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

application {
    mainClass.set("com.github.the_gigi.llm_playground.Main")
}


tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}