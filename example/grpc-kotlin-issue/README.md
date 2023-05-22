## grpc-kotlin-issue

### Sterp 1: Run server

```shell
./gradlew :protocol:generateProto
./gradlew :example:grpc-kotlin-issue:run 
```

### Step 2: Call via grpcurl

```shell
grpcurl -plaintext -d '{"name": "Bob"}' -H 'x-auth-key: client1Key' localhost:8080 example.GreeterApi.SayHello
```

### Step 3: Check log

We can confirm that the armeria context is not propagated.

```
23:55:46.448 [armeria-common-worker-nio-2-1 @coroutine#1] INFO example.grpc_kotlin_issue.AuthCoroutineServerInterceptor -- x-auth-key value is client1. grpcCtx={clientId=null}, armeriaCtx=[sreqId=bcde9477, chanId=f43173b0, raddr=0:0:0:0:0:0:0:1:65263, laddr=0:0:0:0:0:0:0:1:8080][h2c://mq2059x52n2/example.GreeterApi/SayHello#POST]
23:55:46.555 [armeria-common-worker-nio-2-1 @coroutine#1] INFO example.grpc_kotlin_issue.AuthCoroutineServerInterceptor -- Authenticate result is client1. grpcCtx={clientId=null}, armeriaCtx=[sreqId=bcde9477, chanId=f43173b0, raddr=0:0:0:0:0:0:0:1:65263, laddr=0:0:0:0:0:0:0:1:8080][h2c://mq2059x52n2/example.GreeterApi/SayHello#POST]
23:55:46.569 [DefaultDispatcher-worker-1 @coroutine#2] INFO example.grpc_kotlin_issue.GreeterApi -- Receive request. request=name: "Bob"
, grpcCtx={clientId=client1}, armeriaCtx=null
23:55:46.678 [DefaultDispatcher-worker-1 @coroutine#2] INFO example.grpc_kotlin_issue.GreeterApi -- Do some blocking function. grpcCtx={clientId=client1}, armeriaCtx=null
23:55:46.782 [DefaultDispatcher-worker-3 @coroutine#2] INFO example.grpc_kotlin_issue.GreeterApi -- Send response. response=message: "Hello, Bob"
, grpcCtx={clientId=client1}, armeriaCtx=null
```
