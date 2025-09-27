plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.dependency.management)
}
dependencies {
    implementation(projects.common)
    implementation(projects.client)

    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.postgresql)

    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    implementation(libs.spring.security.crypto)

    implementation(libs.kotlin.reflect)

    testImplementation(libs.spring.boot.test)
}