package example.grpc_kotlin_issue

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import io.grpc.ServerInterceptors
import io.grpc.protobuf.services.ProtoReflectionService
import org.slf4j.LoggerFactory
import kotlin.concurrent.thread

private val log = LoggerFactory.getLogger("main")

fun main() {
    System.setProperty("kotlinx.coroutines.debug", "on")

    log.info("Starting server...")

    val server = Server.builder()
        .http(8080)
        .let { configureServices(it) }
        .build()

    Runtime.getRuntime().addShutdownHook(thread(start = false) {
        server.stop().join()
        log.info("Server has been stopped.")
    })

    server.start().join()
    log.info("Started at http://localhost:8080")
}

fun configureServices(sb: ServerBuilder): ServerBuilder {
    return sb
        .service(
            GrpcService.builder()
                .addServiceDefinitions(
                    ServerInterceptors.interceptForward(
                        GreeterApi(),
                        listOf(
                            RequestIdInterceptor(),
                            CoroutineNameContextServerInterceptor(),
                            AuthCustomCoroutineServerInterceptor(),
                        )
                    ),
                )
                .supportedSerializationFormats(GrpcSerializationFormats.values())
                .enableUnframedRequests(true)
                .build(),
        )
        .service(
            GrpcService.builder()
                .addService(ProtoReflectionService.newInstance())
                .build()
        )
        .serviceUnder("/internal/docs", DocService())
}
