package example.grpc_kotlin_issue

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.kotlin.CoroutineContextServerInterceptor
import kotlinx.coroutines.CoroutineName
import kotlin.coroutines.CoroutineContext

class CoroutineNameContextServerInterceptor : CoroutineContextServerInterceptor() {
    override fun coroutineContext(call: ServerCall<*, *>, headers: Metadata): CoroutineContext {
        return CoroutineName("hogehoge")
    }
}
