import com.google.protobuf.gradle.id

plugins {
    id("conventions.common")
    id("com.google.protobuf")
}

dependencies {
    api("io.grpc:grpc-protobuf")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.core.get()}"
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
            }
        }
    }
}
