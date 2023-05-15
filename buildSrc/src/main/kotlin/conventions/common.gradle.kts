package conventions

plugins {
    java
    `java-library`
    `project-report`
}

group = "example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    implementation(platform(libs.armeria.bom))
    implementation(platform(libs.grpc.bom))
    implementation(platform(libs.protobuf.bom))
    implementation(platform(libs.spring.boot.bom))

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
