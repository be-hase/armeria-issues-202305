package conventions

plugins {
    java
    kotlin("jvm")
    `project-report`
}

group = "example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
        )
    }
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    implementation(enforcedPlatform(libs.kotlin.core.bom))
    implementation(enforcedPlatform(libs.kotlin.coroutines.bom))
    implementation(platform(libs.armeria.bom))
    implementation(platform(libs.grpc.bom))
    implementation(platform(libs.protobuf.bom))
    implementation(platform(libs.spring.boot.bom))

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
