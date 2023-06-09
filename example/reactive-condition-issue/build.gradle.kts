plugins {
    id("conventions.common")
    id("org.springframework.boot")
}

dependencies {
    implementation("com.linecorp.armeria:armeria-spring-boot3-starter")

    implementation("org.springframework:spring-webflux")
    implementation("io.projectreactor.netty:reactor-netty")
}
