package com.linecorp.armeria.server.grpc

import io.grpc.Context
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.kotlin.CoroutineContextServerInterceptor
import io.grpc.kotlin.GrpcContextElement
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberProperties

interface CustomCoroutineServerInterceptor : AsyncServerInterceptor {
    @OptIn(DelicateCoroutinesApi::class)
    override fun <I : Any, O : Any> asyncInterceptCall(
        call: ServerCall<I, O>,
        headers: Metadata,
        next: ServerCallHandler<I, O>
    ): CompletableFuture<ServerCall.Listener<I>> {
        return GlobalScope.future(
            COROUTINE_CONTEXT_KEY.get()
                    + GrpcContextElement.current()
        ) {
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
