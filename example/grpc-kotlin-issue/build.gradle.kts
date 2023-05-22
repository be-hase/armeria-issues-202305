plugins {
    id("conventions.common")
    id("application")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation(projects.protocol)
    implementation("com.linecorp.armeria:armeria-grpc")
    implementation("com.linecorp.armeria:armeria-grpc-kotlin")
    implementation("ch.qos.logback:logback-classic")
}

application {
    mainClass.set("example.grpc_kotlin_issue.MainKt")
}
