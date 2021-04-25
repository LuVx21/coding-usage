package org.luvx.grpc.service.sdk.client;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.luvx.grpc.service.proto.HelloRequest;
import org.luvx.grpc.service.proto.HelloRequest.Builder;
import org.luvx.grpc.service.proto.HelloResponse;
import org.luvx.grpc.service.proto.HelloServiceGrpc;
import org.luvx.grpc.service.proto.HelloServiceGrpc.HelloServiceBlockingStub;
import org.luvx.grpc.service.sdk.RpcConfig;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceRpcClientTest {

    HelloServiceRpcClient client;

    @Before
    public void init() {
        client = HelloServiceRpcClient.getClient();
    }

    @Test
    public void sayHelloTest() {
        Builder builder = HelloRequest.newBuilder();
        for (int i = 0; i < 3; i++) {
            HelloRequest request = builder
                    .setFirstName("foo" + i).setLastName("bar" + i)
                    .build();
            HelloResponse response = client.sayHello(request);
            log.info("执行结果: {}", response.getGreeting());
        }
    }

    @Test
    public void sayHello1Test() {
        ManagedChannel channel = null;
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", RpcConfig.port)
                    .usePlaintext()
                    .build();
            HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);
            Builder builder = HelloRequest.newBuilder();
            for (int i = 0; i < 3; i++) {
                HelloRequest request = builder
                        .setFirstName("foo" + i).setLastName("bar" + i)
                        .build();
                HelloResponse response = stub.sayHello(request);
                log.info("执行结果: {}", response.getGreeting());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}