plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
}

group = "org.example.project"
version = "1.0.0"

dependencies {
    implementation(projects.common)
    implementation(projects.client)
    implementation(projects.businessService)

    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.actuator)

    implementation(libs.postgresql)

    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    implementation(libs.jackson.module.kotlin)

    testImplementation(libs.spring.boot.test)
}