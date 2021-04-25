package org.luvx.grpc.service.sdk.client;

import org.luvx.grpc.service.proto.HelloRequest;
import org.luvx.grpc.service.proto.HelloResponse;

public interface HelloServiceRpcClient {
    HelloResponse sayHello(HelloRequest request);

    static HelloServiceRpcClient getClient() {
        return new HelloServiceRpcClientImpl();
    }
}
