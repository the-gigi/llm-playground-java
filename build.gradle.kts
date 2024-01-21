plugins {
    id("java")
}

group = "com.github.the.gigi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //implementation(libs.openai-api)
    implementation("com.theokanning.openai-gpt3-java:api:0.14.0")
    implementation("com.theokanning.openai-gpt3-java:client:0.14.0")
    implementation("com.theokanning.openai-gpt3-java:service:0.14.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    //
}

tasks.test {
    useJUnitPlatform()
}