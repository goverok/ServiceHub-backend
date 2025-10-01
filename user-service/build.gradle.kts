plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.dependency.management)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(projects.common)

    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.postgresql)

    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    implementation(libs.spring.security.crypto)
    implementation(libs.bouncycastle)

    implementation(libs.kotlin.reflect)

    implementation(libs.jsonwebtoken.jjwt.api)
    runtimeOnly(libs.jsonwebtoken.jjwt.impl)
    runtimeOnly(libs.jsonwebtoken.jjwt.jackson)

    testImplementation(libs.spring.boot.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.junit)
}