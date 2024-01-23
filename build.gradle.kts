plugins {
    java
    application
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

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("com.github.the_gigi.llm_playground.MainClass")
}

tasks.test {
    useJUnitPlatform()
}