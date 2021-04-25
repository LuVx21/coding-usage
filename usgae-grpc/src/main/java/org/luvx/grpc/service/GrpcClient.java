package org.luvx.grpc.service;

import java.util.concurrent.TimeUnit;

import org.luvx.grpc.proto.rpc.HelloRequest;
import org.luvx.grpc.proto.rpc.HelloRequest.Builder;
import org.luvx.grpc.proto.rpc.HelloResponse;
import org.luvx.grpc.proto.rpc.HelloServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = null;
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 8090)
                    .usePlaintext()
                    .build();
            HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);
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