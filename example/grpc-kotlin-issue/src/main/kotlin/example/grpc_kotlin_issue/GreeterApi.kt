package example.grpc_kotlin_issue

import com.linecorp.armeria.server.ServiceRequestContext
import example.protocol.GreeterApiGrpcKt
import example.protocol.HelloRequest
import example.protocol.HelloResponse
import example.protocol.helloResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

class GreeterApi : GreeterApiGrpcKt.GreeterApiCoroutineImplBase() {
    override suspend fun sayHello(request: HelloRequest): HelloResponse {
        log.info(
            "Receive request. request={}, grpcCtx={}, armeriaCtx={}",
            request,
            currentGrpcContext(),
            ServiceRequestContext.currentOrNull()
        )

        delay(100)

        withContext(Dispatchers.IO) {
            log.info(
                "Do some blocking function. grpcCtx={}, armeriaCtx={}",
                currentGrpcContext(),
                ServiceRequestContext.currentOrNull(),
            )
            Thread.sleep(100)
        }

        return helloResponse {
            this.message = "Hello, ${request.name}"
        }.also {
            log.info(
                "Send response. response={}, grpcCtx={}, armeriaCtx={}",
                it,
                currentGrpcContext(),
                ServiceRequestContext.currentOrNull()
            )
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(GreeterApi::class.java)
    }
}
