package org.luvx.grpc.service.impl;

import org.luvx.grpc.service.proto.HelloRequest;
import org.luvx.grpc.service.proto.HelloResponse;
import org.luvx.grpc.service.proto.HelloServiceGrpc.HelloServiceImplBase;

import io.grpc.stub.StreamObserver;

public class HelloServiceImpl extends HelloServiceImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        String greeting = "Hello, "
                + request.getFirstName()
                + " "
                + request.getLastName();

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
