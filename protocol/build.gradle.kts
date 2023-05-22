import com.google.protobuf.gradle.id

// Based on https://github.com/grpc/grpc-kotlin/tree/master/compiler#grpc-kotlin-codegen-plugin-for-protobuf-compiler

plugins {
    id("conventions.common")
    id("com.google.protobuf")
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    api(libs.protobuf.kotlin)
    api("io.grpc:grpc-protobuf")
    api(libs.grpc.kotlin)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.core.get()}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${libs.versions.grpc.kotlin.get()}:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            // https://armeria.dev/docs/server-docservice/#adding-docstrings
            task.generateDescriptorSet = true
            task.descriptorSetOptions.apply {
                includeSourceInfo = true
                includeImports = true
                path = "${project.buildDir}/resources/main/META-INF/armeria/grpc/${project.name}.dsc"
            }

            task.plugins {
                id("grpc")
                id("grpckt")
            }
            task.builtins {
                id("kotlin")
            }
        }
    }
}
