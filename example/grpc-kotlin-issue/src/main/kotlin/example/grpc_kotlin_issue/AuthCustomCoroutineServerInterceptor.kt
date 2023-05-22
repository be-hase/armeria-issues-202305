package example.grpc_kotlin_issue

import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.grpc.CustomCoroutineServerInterceptor
import io.grpc.*
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

class AuthCustomCoroutineServerInterceptor : CustomCoroutineServerInterceptor {
    override suspend fun <ReqT, RespT> suspendedInterceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val clientId = authenticate(headers) ?: return responseError(call)

        // Set clientId into grpc context and armeria context
        val ctx = Context.current().withValue(CLIENT_ID_GRPC_CONTEXT_KEY, clientId)
        ServiceRequestContext.currentOrNull()?.setAttr(CLIENT_ID_ARMERIA_CONTEXT_KEY, clientId)

        return Contexts.interceptCall(ctx, call, headers, next)
    }

    private suspend fun authenticate(headers: Metadata): String? {
        val value = CLIENT_MAP[headers.get(AUTH_METADATA_KEY)]
        log.info(
            "x-auth-key value is {}. grpcCtx={}, armeriaCtx={}",
            value,
            currentGrpcContext(),
            ServiceRequestContext.currentOrNull()
        )

        // we can use suspend function :)
        delay(100)

        return value.also {
            log.info(
                "Authenticate result is {}. grpcCtx={}, armeriaCtx={}",
                it,
                currentGrpcContext(),
                ServiceRequestContext.currentOrNull()
            )
        }
    }

    private fun <ReqT, RespT> responseError(call: ServerCall<ReqT, RespT>): ServerCall.Listener<ReqT> {
        call.close(Status.UNAUTHENTICATED.withDescription("invalid key"), Metadata())
        return object : ServerCall.Listener<ReqT>() {
            // noop
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(AuthCustomCoroutineServerInterceptor::class.java)
    }
}
