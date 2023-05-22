package example.grpc_kotlin_issue

import com.linecorp.armeria.server.ServiceRequestContext
import io.grpc.*
import java.util.*

class RequestIdInterceptor : ServerInterceptor {
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val id = UUID.randomUUID().toString()
        val ctx = Context.current().withValue(REQUEST_ID_GRPC_CONTEXT_KEY, id)
        ServiceRequestContext.currentOrNull()?.setAttr(REQUEST_ID_ARMERIA_CONTEXT_KEY, id)
        return Contexts.interceptCall(ctx, call, headers, next)
    }
}
