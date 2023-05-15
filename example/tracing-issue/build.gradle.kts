plugins {
    id("conventions.common")
    id("org.springframework.boot")
}

dependencies {
    implementation("com.linecorp.armeria:armeria-spring-boot3-starter")
    implementation("com.linecorp.armeria:armeria-spring-boot3-actuator-starter")
    implementation("com.linecorp.armeria:armeria-brave")
    implementation("io.lettuce:lettuce-core")
    implementation("io.projectreactor:reactor-core")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
}
