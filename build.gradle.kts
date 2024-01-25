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
    implementation(libs.openai.api)
    implementation(libs.openai.client)
    implementation(libs.openai.service)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.jackson)
    implementation(libs.okhttp)
    implementation(libs.jackson.databind)
    //implementation(libs.openai_kotlin.openai.client)
    implementation("com.aallam.openai:openai-client:3.6.3")
    implementation("io.ktor:ktor-client-okhttp:2.3.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.slf4j:slf4j-api:2.0.11") // SLF4J API
    implementation("ch.qos.logback:logback-classic:1.4.14") // Logback implementation


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