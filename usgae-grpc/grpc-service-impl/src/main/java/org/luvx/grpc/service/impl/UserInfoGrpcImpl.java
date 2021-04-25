package org.luvx.grpc.service.impl;

import org.luvx.grpc.service.proto.user.UserInfoGrpc.UserInfoImplBase;
import org.luvx.grpc.service.proto.user.UserRequest;
import org.luvx.grpc.service.proto.user.UserResponse;

import io.grpc.stub.StreamObserver;

public class UserInfoGrpcImpl extends UserInfoImplBase {
    @Override
    public void selectUserInfo(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        String greeting = "Hello, "
                + request.getId() + " "
                + request.getName() + " "
                + request.getPassword() + " "
                + request.getAge();

        UserResponse response = UserResponse.newBuilder()
                .setMessage("查询: " + greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response = UserResponse.newBuilder()
                .setMessage("更新...")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
