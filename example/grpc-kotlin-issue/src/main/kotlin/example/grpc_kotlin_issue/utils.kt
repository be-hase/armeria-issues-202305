package example.grpc_kotlin_issue

import io.grpc.Context
import io.grpc.Metadata
import io.netty.util.AttributeKey

val AUTH_METADATA_KEY: Metadata.Key<String> = Metadata.Key.of("x-auth-key", Metadata.ASCII_STRING_MARSHALLER)

val CLIENT_ID_GRPC_CONTEXT_KEY: Context.Key<String> = Context.key("clientId")
val CLIENT_ID_ARMERIA_CONTEXT_KEY: AttributeKey<String> = AttributeKey.valueOf("clientId")
val CLIENT_MAP = mapOf(
    "client1Key" to "client1",
    "client2Key" to "client2",
    "client3Key" to "client3",
)

fun currentGrpcContext(): Map<String, Any> {
    return mapOf(
        "clientId" to CLIENT_ID_GRPC_CONTEXT_KEY.get(),
    )
}
