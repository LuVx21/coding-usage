package org.luvx.grpc.service.sdk.client;

import org.luvx.grpc.service.proto.HelloRequest;
import org.luvx.grpc.service.proto.HelloResponse;
import org.luvx.grpc.service.proto.HelloServiceGrpc;
import org.luvx.grpc.service.proto.HelloServiceGrpc.HelloServiceBlockingStub;
import org.luvx.grpc.service.sdk.RpcConfig;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceRpcClientImpl implements HelloServiceRpcClient {
    private static final HelloServiceBlockingStub stub;

    static {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", RpcConfig.port)
                .usePlaintext()
                .build();
        stub = HelloServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public HelloResponse sayHello(HelloRequest request) {
        return stub.sayHello(request);
    }
}