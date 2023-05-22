package com.linecorp.armeria.server.grpc

import io.grpc.Context
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.kotlin.CoroutineContextServerInterceptor
import io.grpc.kotlin.GrpcContextElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties

interface CorrectCoroutineServerInterceptor : AsyncServerInterceptor {
    override fun <I : Any, O : Any> asyncInterceptCall(
        call: ServerCall<I, O>,
        headers: Metadata,
        next: ServerCallHandler<I, O>
    ): CompletableFuture<ServerCall.Listener<I>> {
        return CoroutineScope(
            // It is necessary to write this to propagate the CoroutineContext set by the previous interceptor.
            // (AThe ArmeriaRequestCoroutineContext is also propagated by this)
            COROUTINE_CONTEXT_KEY.get()
                    // gRPC Context must also be propagated
                    + GrpcContextElement.current()
        ).future {
            suspendedInterceptCall(call, headers, next)
        }
    }

    suspend fun <ReqT, RespT> suspendedInterceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT>

    companion object {
        @Suppress("UNCHECKED_CAST")
        // Get by using reflection
        internal val COROUTINE_CONTEXT_KEY: Context.Key<CoroutineContext> =
            CoroutineContextServerInterceptor::class.let { kclass ->
                val companionObject = kclass.companionObject!!
                val property = companionObject.memberProperties.single { it.name == "COROUTINE_CONTEXT_KEY" }
                checkNotNull(property.getter.call(kclass.companionObjectInstance!!)) as Context.Key<CoroutineContext>
            }
    }
}
